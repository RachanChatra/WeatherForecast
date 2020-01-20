package com.example.weatherforecast.common.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentHelper {

    companion object {

        /**
         * This method pops all the fragments which matches with the specified tag from back-stack and
         * replaces the given fragment.
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         * @param tag optional tag name for the fragment
         */
        fun popBackStackAndReplace(mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            mgr.beginTransaction().replace(id, frgmt, tag).commit()
        }

        /**
         * This method adds current fragment to back-stack and replace it with given fragment.
         * Specify the fragment tag.
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         * @param tag optional tag name for the fragment
         */
        fun addToBackStackAndReplace(mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction().addToBackStack(null).replace(id, frgmt, tag).commit()
        }

        /**
         * This method adds current fragment to back-stack and replace it with given fragment.
         * This replaces fragment with animation. Specify the fragment tag.
         *
         * @param mgr
         * @param id
         * @param tag
         * @param frgmt
         */
        fun addToBackStackAndReplaceWithAnimation(mgr: FragmentManager, id: Int,
                                                  frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction()
                /*.setCustomAnimations(R.anim.enter_from_right,
                    R.anim.exit_to_left, R.anim.enter_from_left,
                    R.anim.exit_to_right)*/.addToBackStack(null)
                .replace(id, frgmt, tag).commit()
        }

        /**
         * Replace child fragment with specifying tag.
         *
         * @param parentFragment
         * @param containerId
         * @param childFragment
         * @param tag optional tag name for the fragment
         */
        private fun replaceChildFragment(parentFragment: Fragment?, containerId: Int, childFragment: Fragment?, tag: String? = null) {
            if (childFragment != null && parentFragment != null
                && parentFragment.activity != null
                && parentFragment.activity?.isFinishing == false) {
                val manager = parentFragment.childFragmentManager
                val transaction = manager.beginTransaction()
                transaction.replace(containerId, childFragment, tag)
                transaction.commitAllowingStateLoss()
            }
        }

        /**
         * Adds all the fragments to back-stack and replace the given fragment without allowing state loss.
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         * @param tag optional tag name for the fragment
         */
        fun replace(mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.popBackStack()
            mgr.beginTransaction().replace(id, frgmt, tag).addToBackStack(null).commit()

        }

        /**
         * Adds the fragment to back-stack
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         * @param tag optional tag name for the fragment
         */
        fun add(mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction().add(id, frgmt, tag).commitAllowingStateLoss()
        }

        /**
         * This method will replace multiple fragments with the passed fragment.
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         * @param count count
         * @param tag optional tag name for the fragment
         */
        fun replaceMultiple(mgr: FragmentManager, id: Int, frgmt: Fragment, count: Int, tag: String? = null) {
            if (mgr.backStackEntryCount < count)
                return

            for (i in 0..(count - 1))
                mgr.popBackStack()

            mgr.beginTransaction().replace(id, frgmt, tag).addToBackStack(null).commit()
        }

        /**
         * Replace the given fragment without adding it to back-stack and allows state loss.
         *
         * @param mgr
         * @param id
         * @param frgmt
         * @param tag optional tag name for the fragment
         */
        fun replaceWithoutAddingToBackStackAllowingStateloss(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction().replace(id, frgmt, tag).commitAllowingStateLoss()
        }

        /**
         * Replace the given fragment without adding it to back-stack and allows state loss.
         *
         * @param mgr
         * @param id
         * @param frgmt
         * @param tag optional tag name for the fragment
         */
        fun replaceWithoutAddingToBackStack(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction().replace(id, frgmt, tag).commit()
        }

        /**
         * Clears fragment back-stack.
         *
         * @param mgr
         * @param flag
         */
        private fun clearBackStack(mgr: FragmentManager, flag: Int) {
            if (mgr.backStackEntryCount > 0) {
                val first = mgr.getBackStackEntryAt(0)
                mgr.popBackStackImmediate(first.id, flag)
            }
        }

        /**
         * Function which pops out the current fragment
         * @param mgr
         */
        fun popCurrentFragment(mgr: FragmentManager) {
            mgr.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        /**
         * Clears fragment back-stack and replaces the given fragment without allowing state loss.
         *
         * @param mgr
         * @param id
         * @param frgmt
         * @param tag optional tag name for the fragment
         */
        fun clearBackStackAndReplace(mgr: FragmentManager, id: Int,
                                     frgmt: Fragment, tag: String? = null) {
            clearBackStack(mgr, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            mgr.beginTransaction().replace(id, frgmt, tag).commit()
        }

        /**
         * Clears fragment back-stack and replaces the given fragment allowing state loss.
         *
         * @param mgr
         * @param id
         * @param frgmt
         * @param tag
         */
        fun clearBackStackAndReplaceAllowingStateLoss(mgr: FragmentManager, id: Int,
                                                      frgmt: Fragment, tag: String? = null) {
            clearBackStack(mgr, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            mgr.beginTransaction().replace(id, frgmt, tag).commitAllowingStateLoss()
        }

        /**
         * Adds current fragment to back-stack and adds given fragment over the current fragment with animation .
         *
         * @param mgr Fragment manager instance
         * @param id container id
         * @param frgmt fragment to be added
         * @param enterAnimation respective animation id that appears when fragment is laoded
         * @param exitAnimation animation to be loaded when fragment exits
         * @param tag optional tag name for the fragment
         */
        fun addToBackStackAndAddWithAnimation(
            mgr: FragmentManager, id: Int, frgmt: Fragment, enterAnimation: Int, exitAnimation: Int, tag: String? = null) {
            mgr.beginTransaction().setCustomAnimations(enterAnimation, exitAnimation, enterAnimation, exitAnimation)
                .addToBackStack(null)
                .add(id, frgmt, tag).commit()
        }

        fun addToBackStackAndAddWithSlideUpAnimation(
            mgr: FragmentManager, id: Int, frgmt: Fragment) {
            mgr.beginTransaction()/*.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down)*/
                .addToBackStack(null)
                .add(id, frgmt).commit()
        }

        /**
         * This method adds given fragment to back-stack
         * @param mgr Fragment Manager
         * @param id container view Id
         * @frgmt Fragment to be added
         * @param tag optional tag name for the fragment
         */
        fun addToBackStackAndAddWithoutReplacingAndAnimation(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction()
                .addToBackStack(null)
                .add(id, frgmt, tag).commit()

        }

        fun addToBackStackAndAddWithoutReplacingWithAnimation(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String) {
            mgr.beginTransaction()/*.setCustomAnimations(R.anim.enter_from_right,
                R.anim.exit_to_left, R.anim.enter_from_left,
                R.anim.exit_to_right)*/
                .addToBackStack(tag)
                .add(id, frgmt).commit()

        }

        /**
         * This method adds given fragment to back-stack
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment to be added
         * @param tag Tag name
         */
        fun addToBackStackAndAddWithoutReplacing(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction()
                .addToBackStack(tag)
                .add(id, frgmt).commit()
        }

        /**
         * Adds given fragment to back-stack without allowing state loss and without animations.
         *
         * @param mgr Fragment Manager
         * @param id container view id
         * @param frgmt Fragment to be added
         * @param tag optional tag name for the fragment
         */
        fun addToBackStackAndAddWithoutAnimation(
            mgr: FragmentManager, id: Int, frgmt: Fragment, tag: String? = null) {
            mgr.beginTransaction().add(id, frgmt, tag).commit()
        }

        /**
         * This method will pop multiple fragments
         *
         * @param mgr Fragment Manager
         * @param count Number of fragments to be pooped.
         */
        fun popMultiple(mgr: FragmentManager, count: Int) {
            for (i in 0..(count - 1))
                mgr.popBackStack()
        }


        /**
         * Adds the fragment to backstack with animation
         *
         * @param mgr Fragment Manager
         * @param id container view Id
         * @param frgmt Fragment
         */
        fun addAnimation(mgr: FragmentManager, id: Int, frgmt: Fragment, fromId: Int, toId: Int) {
            mgr.beginTransaction().setCustomAnimations(fromId, toId).add(id, frgmt).commitAllowingStateLoss()
        }
    }
}
