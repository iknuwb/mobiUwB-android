package pl.edu.uwb.mobiuwb.firebaseNotyfication;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 *  Klasa odpowiadająca za otrzymanie tokenu przy rejestracji aplikacji.
 */

public class UwbFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){
        String token = FirebaseInstanceId.getInstance().getToken();
    }
}
