package pl.edu.uwb.mobiuwb.firebasenotyfication;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 *  Klasa odpowiadająca za otrzymanie tokenu przy rejestracji aplikacji. Tymczasowo nigdzie nie używana.
 */

public class UwbFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh(){
        String token = FirebaseInstanceId.getInstance().getToken();
    }
}
