package dk.itu.moapd.scootersharing.oska.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * A view model sensitive to changes in the `MainActivity` UI components.
 */
class MainActivityVM : ViewModel() {

    /**
     * The current selected camera characteristics.
     */
    private var _characteristics = MutableLiveData<Int>()

    /**
     * A `LiveData` which publicly exposes any update in the camera characteristics.
     */
    val characteristics: LiveData<Int>
        get() = _characteristics

    /**
     * This method will be executed when the user interacts with the camera selector component. It
     * sets the selector into the LiveData instance.
     *
     * @param characteristics A set of requirements and priorities used to select a camera.
     */
    fun onCameraCharacteristicsChanged(characteristics: Int) {
        this._characteristics.value = characteristics
    }

    /**
     * The current selected method.
     */
    private var _methodId = MutableLiveData<Int>()

    /**
     * A `LiveData` which publicly exposes any update in the selected method.
     */
    val methodId: LiveData<Int>
        get() = _methodId

    /**
     * This method will be executed when the user interacts with the method selector component. It
     * sets the method ID into the LiveData instance.
     *
     * @param methodId The selected image analysis method ID.
     */
    fun onMethodChanged(methodId: Int) {
        this._methodId.value = methodId
    }

}