package planto_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import planto_project.dao.AccountRepository;
import planto_project.dto.AuthRequestDto;
import planto_project.dto.AuthResponseDto;
import planto_project.model.UserAccount;
import planto_project.security.JwtUtil;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto>login(@RequestBody AuthRequestDto authRequestDto) throws Exception {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequestDto.getLogin(), authRequestDto.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(authRequestDto.getLogin());
        UserAccount user = accountRepository.findById(userDetails.getUsername()).orElseThrow( () ->
                new RuntimeException("Account not found"));
        String token = jwtUtil.generateJwtToken(userDetails.getUsername(), user);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
