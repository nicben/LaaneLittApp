package com.laanelitt.laanelittapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.fragment_item.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.laanelitt.laanelittapp.databinding.FragmentItemBinding


//profil til item - info

class ItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding1 = DataBindingUtil.inflate<FragmentItemBinding>(inflater,R.layout.fragment_item,container,false)


        binding1.btn.setOnClickListener{
            binding1.btn.setOnClickListener { view : View ->
                view.findNavController().navigate(R.id.action_itemFragment_to_loanRequestPopupFragment)
            }
        }
        return binding1.root

    }






}