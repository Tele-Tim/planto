package planto_project.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import planto_project.dao.AccountRepository;
import planto_project.model.UserAccount;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = accountRepository.findById(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        Collection<String> authorities = user.getRoles().stream()
                .map(r -> "ROLE_" + r.name())
                .toList();
        return new User(username, user.getPassword(),
                AuthorityUtils.createAuthorityList(authorities));
    }
}
