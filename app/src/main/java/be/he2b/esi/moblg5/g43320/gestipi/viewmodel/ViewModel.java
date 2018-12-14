package be.he2b.esi.moblg5.g43320.gestipi.viewmodel;

/**
 * Interface that must implement all the view model class
 * A viewModel links the model to the view
 */
public interface ViewModel {

    /**
     * Method called when the screen is visited for the first time
     */
    void onCreate();

    /**
     * Method called when coming back to the screen
     */
    void onResume();
}
