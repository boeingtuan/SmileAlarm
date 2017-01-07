package yolo.bachkhoa.com.smilealarm.Service;

/**
 * Created by Tuân on 07/01/2017.
 */

public class AuthenticateService {
    private AuthenticateService(){}
    private static AuthenticateService authenticateService = new AuthenticateService();
    public static AuthenticateService getInstance(){
        return authenticateService;
    }
    public boolean checkUserLogin(){
        return true;
    }
}
