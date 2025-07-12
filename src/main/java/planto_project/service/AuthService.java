package planto_project.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import planto_project.dto.AuthRequestDto;
import planto_project.dto.AuthResponseDto;

@Service
public interface AuthService {
    AuthResponseDto login(AuthRequestDto authRequestDto, HttpServletResponse response);

    AuthResponseDto refresh(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);
}
