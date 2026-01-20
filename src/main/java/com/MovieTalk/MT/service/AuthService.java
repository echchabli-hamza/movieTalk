package com.MovieTalk.MT.service;


import com.MovieTalk.MT.dto.LoginRequest;
import com.MovieTalk.MT.dto.LoginResponse;
import com.MovieTalk.MT.dto.RegisterRequest;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse register(RegisterRequest request);
}
