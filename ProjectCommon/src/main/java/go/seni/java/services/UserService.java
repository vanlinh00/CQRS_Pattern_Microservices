package go.seni.java.services;

import go.seni.java.entity.User;

public interface UserService {
    User getById(Long id);
    User getByLoginId(String loginId);
     User getCurrentUser() ;

}