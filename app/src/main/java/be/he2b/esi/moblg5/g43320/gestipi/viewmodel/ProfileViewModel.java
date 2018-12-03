package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

import android.util.Log;
import android.widget.Spinner;

import java.util.Observable;
import java.util.Observer;

import be.he2b.esi.moblg5.g43320.gestipi.pojo.User;

public class ProfileViewModel implements Observer, ViewModel {

    private User user;

    public ProfileViewModel(User user){
        this.user = user;
        user.addObserver(this);
    }

    @Override
    public void onCreate() {
        Log.d("ProfileViewModel", "onCreate");
    }

    @Override
    public void onPause() {
        Log.d("ProfileViewModel", "onPause");
    }

    @Override
    public void onResume() {
        Log.d("ProfileViewModel", "onResume");
    }

    @Override
    public void onDestroy() {
        Log.d("ProfileViewModel", "onDestroy");
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    public void onClickUpdateButton(){

    }

    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return -1;
    }

    public User getUser() {
        return user;
    }
}
