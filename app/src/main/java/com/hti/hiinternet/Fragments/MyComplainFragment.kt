package com.hti.hiinternet.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout

import com.hti.hiinternet.R
import com.hti.hiinternet.adapter.MycomplainServiceAdapter
import com.hti.hiinternet.adapter.PaymentAdapter
import com.hti.hiinternet.base.BaseFrgment
import com.hti.hiinternet.data.NetworkResult
import com.hti.hiinternet.data.Status
import com.hti.hiinternet.data.request.RequestCheckCreateUser
import com.hti.hiinternet.data.request.RequestMycomplainService
import com.hti.hiinternet.data.response.ResponseMycomplainService
import com.hti.hiinternet.data.response.ResponsePayment
import com.hti.hiinternet.util.Constant
import com.hti.hiinternet.util.ui.HtiLinearLayoutManager
import com.hti.hiinternet.viewModel.CheckCreateUserViewModel
import com.hti.hiinternet.viewModel.MycomplainServiceViewModel
import com.hti.hiinternet.viewModel.PaymentViewModel
import kotlinx.android.synthetic.main.fragment_my_complain.*
import kotlinx.android.synthetic.main.fragment_payment.*


class MyComplainFragment : BaseFrgment() {

    lateinit var myComplainViewModel: MycomplainServiceViewModel
    lateinit var complainLayoutmanager: LinearLayoutManager
    lateinit var recMycomplain: RecyclerView
    lateinit var myComplainAdapter: MycomplainServiceAdapter
    lateinit var shrimmerLayout: ShimmerFrameLayout
    lateinit var mCheckCreateUserViewModel: CheckCreateUserViewModel

    companion object {
        fun newIntance() = MyComplainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_complain, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        complainLayoutmanager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        myComplainViewModel = getViewModel()

        mCheckCreateUserViewModel = getCheckCreateUserViewModel()


        recMycomplain = view.findViewById(R.id.recycler_myComplain)
        recMycomplain.layoutManager = complainLayoutmanager
        myComplainAdapter = MycomplainServiceAdapter(ArrayList())
        recMycomplain.adapter = myComplainAdapter
        recMycomplain.setHasFixedSize(true)
        observeViewModel(myComplainViewModel)
        myComplainViewModel.loadMycomplainService(getMycomplainRequest())
        shrimmerLayout = view.findViewById(R.id.loading_layout)

        observerViewModel(mCheckCreateUserViewModel)


        btn_add.setOnClickListener {
           mCheckCreateUserViewModel.loadAccountData(getCheckCreateUserRequest())
        }

    }

    private fun observerViewModel(mCheckCreateUserViewModel: CheckCreateUserViewModel) {
        mCheckCreateUserViewModel.status?.observe(this, Observer {
            when (it) {
                Status.ERROR -> {
                    Log.d("Fb Id Error", "Fail fb id")
                }
                //show Error here
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    Log.d("Created user success", it.toString())

                }
            }
        })

        mCheckCreateUserViewModel.createUserResponseLiveData?.observe(this
            , Observer {
                Log.d("CheckCreateUser", it.checkCreateUser.toString())
                it.checkCreateUser.let {
                    if (it == "fail") {
                        failCreateUserDialog(
                            "Fail",
                            getString(R.string.str_txt_dialog_fail_create_user)
                        )
                    } else {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.main_frame, SendComplainFragment.newIntance(), null)
                            ?.commit()
                    }


                }
            })
    }


    private fun getMycomplainRequest(): RequestMycomplainService {

        return RequestMycomplainService()
    }

    private fun observeViewModel(v: MycomplainServiceViewModel) {

        v.status!!.observe(this, Observer<Status> {
            when (it) {
                Status.SUCCESS -> {
                    Log.d("MyComplain Service", "success")

                    showNormalView()
                }
                Status.ERROR -> {

                    getApiErrorDialog("Api Fail", Constant.FAIL_TO_CONNECT_API)
                    Log.d("MyComplainFragment", "error")

                }
                Status.LOADING -> {
                    showLoadingView()
                }

            }

            v.myComplainResponseLiveData?.observe(
                this,
                Observer<NetworkResult<ArrayList<ResponseMycomplainService>>> {


                    myComplainAdapter.setData(it.data)

                })

        })

    }

    override fun showLoadingView() {
        normal_layout.visibility = View.GONE
        loading_layout.visibility = View.VISIBLE
        shrimmerLayout.startShimmer()
    }

    override fun showNormalView() {
        normal_layout.visibility = View.VISIBLE
        loading_layout.visibility = View.GONE
        shrimmerLayout.stopShimmer()
    }

    private fun getCheckCreateUserViewModel(): CheckCreateUserViewModel {
        return ViewModelProviders.of(this).get(CheckCreateUserViewModel::class.java)
    }

    private fun getViewModel(): MycomplainServiceViewModel {
        return ViewModelProviders.of(this).get(MycomplainServiceViewModel::class.java)
    }

    private fun getCheckCreateUserRequest() : RequestCheckCreateUser {
        return RequestCheckCreateUser()
    }


}
