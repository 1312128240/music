package com.example.baidumusic2.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class MyBaseFragment<T:ViewDataBinding>:Fragment() {

    lateinit var mContext:Context

    var fragmentBind: T?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        println("${this.javaClass}--->onAttach")
        mContext=context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("${this.javaClass}---->onCreateView")
        fragmentBind=DataBindingUtil.inflate(inflater,getLayoutId(),null,false)
        return fragmentBind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        business()
        println("${this.javaClass}---->onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        println("${this.javaClass}---->onActivityCreated")
    }


    override fun onDetach() {
        super.onDetach()
        println("${this.javaClass}---->onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        println("${this.javaClass}---->onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("${this.javaClass}---->onDestroy")
    }


    abstract fun getLayoutId():Int


    abstract fun business()
}