package com.example.KTGK.config;

import com.example.KTGK.model.Role;
import com.example.KTGK.model.Student;
import com.example.KTGK.repository.RoleRepository;
import com.example.KTGK.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");

        Optional<Student> existingStudent = studentRepository.findByEmail(email);
        Student student;

        if (existingStudent.isEmpty()) {
            student = new Student();
            student.setEmail(email);
            student.setUsername(email);
            student.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
            studentRepository.save(student);

            Role studentRole = roleRepository.findByName("STUDENT")
                    .orElseThrow(() -> new RuntimeException("Role STUDENT not found"));
            student.getRoles().add(studentRole);
            studentRepository.save(student);
        } else {
            student = existingStudent.get();
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        student.getRoles().forEach(role ->
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("username", student.getUsername());

        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
