package com.geoly.app.services;

import com.geoly.app.models.CustomUserDetails;
import com.geoly.app.models.Role;
import com.geoly.app.models.User;
import com.geoly.app.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for (Role role : user.get().getRole()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName().name()));
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user.get());
        customUserDetails.setAuthorities(grantedAuthorities);
        return customUserDetails;
    }




}
