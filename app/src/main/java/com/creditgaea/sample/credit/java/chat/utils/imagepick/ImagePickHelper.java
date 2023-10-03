package com.creditgaea.sample.credit.java.chat.utils.imagepick;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.creditgaea.sample.credit.java.chat.utils.imagepick.fragment.ImageSourcePickDialogFragment;
import com.creditgaea.sample.credit.java.chat.utils.imagepick.fragment.ImagePickHelperFragment;


public class ImagePickHelper {

    public void pickAnImage(FragmentActivity activity, int requestCode) {
        ImagePickHelperFragment imagePickHelperFragment = ImagePickHelperFragment.start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), imagePickHelperFragment);
    }

    private void showImageSourcePickerDialog(FragmentManager fm, ImagePickHelperFragment fragment) {
        ImageSourcePickDialogFragment.show(fm,
                new ImageSourcePickDialogFragment.LoggableActivityImageSourcePickedListener(fragment));
    }
}