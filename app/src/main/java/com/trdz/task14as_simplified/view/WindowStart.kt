package com.trdz.task14as_simplified.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
import com.trdz.task14as_simplified.base_utility.EFFECT_RISE
import com.trdz.task14as_simplified.databinding.FragmentWindowStartBinding
import com.trdz.task14as_simplified.view.segment_users.WindowPicture

class WindowStart: Fragment() {

	//region Elements
	private var _executors: Leader? = null
	private val executors get() = _executors!!
	private var _binding: FragmentWindowStartBinding? = null
	private val binding get() = _binding!!
	//endregion

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		_executors = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentWindowStartBinding.inflate(inflater, container, false)
		_executors = (requireActivity() as MainActivity)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.firstView.animate().apply {
			alpha(0.0f)
			duration = 2900L
			withEndAction { requireActivity().supportFragmentManager.beginTransaction().detach(this@WindowStart).commit()  }
			start()
		}
		createMainWindow()
	}

	private fun createMainWindow() {
		Handler(Looper.getMainLooper()).postDelayed({
			executors.getNavigation().replace(requireActivity().supportFragmentManager,WindowPicture(),false,effect = EFFECT_RISE)
		}, 100L)
	}

	companion object {
		fun newInstance() = WindowStart()
	}
}

