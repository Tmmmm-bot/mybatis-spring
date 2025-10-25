package com.example.exp3.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.exp3.dto.LoginRequest;
import com.example.exp3.entity.User;
import com.example.exp3.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        if (request == null
                || !StringUtils.hasText(request.getUsername())
                || !StringUtils.hasText(request.getPassword())) {
            return ResponseEntity.badRequest().body(error("用户名和密码不能为空"));
        }

        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error("用户不存在"));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error("密码错误"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "登录成功");
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("username", user.getUsername());
        response.put("user", userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody LoginRequest request) {
        if (request == null
                || !StringUtils.hasText(request.getUsername())
                || !StringUtils.hasText(request.getPassword())) {
            return ResponseEntity.badRequest().body(error("用户名和密码不能为空"));
        }

        long existing = userMapper.selectCount(new QueryWrapper<User>().eq("username", request.getUsername()));
        if (existing > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error("用户名已存在"));
        }

        User user = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        userMapper.insert(user);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "注册成功");
        response.put("username", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", message);
        body.put("success", false);
        return body;
    }
}
