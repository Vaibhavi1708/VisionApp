package com.example.vision.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class ActivityUtils {

    /**
     * Adds the Fragment into layout container
     *
     * @param activity                    The target activity
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param currentFragment             Current loaded Fragment to be hide
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public static boolean addFragment(final FragmentActivity activity, final int fragmentContainerResourceId, final Fragment currentFragment, final Fragment nextFragment) throws IllegalStateException {
        if (activity == null || currentFragment == null || nextFragment == null) {
            return false;
        }
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());
        fragmentTransaction.addToBackStack(nextFragment.getClass().getSimpleName());

        fragmentTransaction.hide(currentFragment);

        fragmentTransaction.commit();

        return true;
    }

    /**
     * Replaces the Fragment into layout container
     *
     * @param activity                    The target activity
     * @param fragmentContainerResourceId Resource id of the layout in which Fragment will be added
     * @param nextFragment                New Fragment to be loaded into fragmentContainerResourceId
     * @return true if new Fragment added successfully into container, false otherwise
     * @throws IllegalStateException Exception if Fragment transaction is invalid
     */
    public static boolean replaceFragment(final FragmentActivity activity, final int fragmentContainerResourceId, final Fragment nextFragment) throws IllegalStateException {
        if (activity == null || nextFragment == null) {
            return false;
        }
        final FragmentManager fragmentManager = activity.getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(fragmentContainerResourceId, nextFragment, nextFragment.getClass().getSimpleName());

        fragmentTransaction.commit();

        return true;
    }
}
