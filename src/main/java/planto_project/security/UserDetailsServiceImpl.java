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
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserAccount user = accountRepository.findById(login)
                .orElseThrow(() -> new UsernameNotFoundException(login));
        Collection<String> roles = user.getRoles().stream()
                .map(r -> "ROLE_" + r.name())
                .toList();
        return new User(login, user.getPassword(), AuthorityUtils.createAuthorityList(roles));
    }
}
