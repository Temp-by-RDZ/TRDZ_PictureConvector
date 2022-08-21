package com.trdz.task14as_simplified.view.segment_picture

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import coil.request.ImageRequest
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.trdz.task14as_simplified.R
import com.trdz.task14as_simplified.base_utility.loadSvg
import com.trdz.task14as_simplified.databinding.FragmentWindowUserListBinding
import com.trdz.task14as_simplified.presenter.MainPresenter
import com.trdz.task14as_simplified.view.Leader
import com.trdz.task14as_simplified.view.MainActivity
import moxy.MvpAppCompatFragment
import moxy.ktx.moxyPresenter

class WindowPicture: MvpAppCompatFragment(), MainView {

	//region Elements
	private var _binding: FragmentWindowUserListBinding? = null
	private val binding get() = _binding!!
	private var _executors: Leader? = null
	private val executors get() = _executors!!
	private var _bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>? = null
	private val bottomSheetBehavior get() = _bottomSheetBehavior!!
	private val presenter by moxyPresenter { MainPresenter() }

	//endregion

	//region Base realization
	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		_executors = null
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		_binding = FragmentWindowUserListBinding.inflate(inflater, container, false)
		_executors = (requireActivity() as MainActivity)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		buttonBinds()
	}

	//endregion


	//region Main functional
	private fun buttonBinds() {
		with(binding) {
			_bottomSheetBehavior = BottomSheetBehavior.from(popupSheet.bottomSheetContainer)
			imageView.setOnClickListener { bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED }
			bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
			bottomSheetBehavior.isHideable = true
			bottomSheetBehavior.addBottomSheetCallback(object:
				BottomSheetBehavior.BottomSheetCallback() {
				override fun onStateChanged(bottomSheet: View, newState: Int) {
					when (newState) {
						BottomSheetBehavior.STATE_DRAGGING -> {
						}
						BottomSheetBehavior.STATE_COLLAPSED -> {
							bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
						}
						BottomSheetBehavior.STATE_EXPANDED -> {
						}
						BottomSheetBehavior.STATE_HALF_EXPANDED -> {
						}
						BottomSheetBehavior.STATE_HIDDEN -> {
						}
						BottomSheetBehavior.STATE_SETTLING -> {
						}
					}
				}

				override fun onSlide(bottomSheet: View, slideOffset: Float) {}

			})
		}
	}
	//endregion

	//region Presenter command realization

	override fun onSuccess(name: String, desc: String, url: String) {
		Log.d("@@@", "App - render")
		with(binding) {
			imageView.setBackgroundResource(R.drawable.plaseholder)
			imageView.load(url) {
				listener(
					onSuccess = { _, _ ->
						megaButton.text = getString(R.string.status_load)
					},
					onError = { request: ImageRequest, throwable: Throwable ->
						Log.d("@@@", "App - coil error $throwable")
						imageView.loadSvg(url) //если вдруг coil помрет
					})
			}
			popupSheet.title.text = name
			popupSheet.explanation.text = desc
		}
	}

	override fun onError(code: Int, error: Throwable?) = with(binding) {
		Log.d("@@@", "App - catch $code")
		executors.getExecutor().showToast(requireContext(), "Ошибка...")
		megaButton.text = getString(R.string.status_error)
		if (code > -3) {
			imageView.setBackgroundResource(R.drawable.nofile)
			popupSheet.title.text = getString(R.string.ERROR_TITLE)
			popupSheet.explanation.text = StringBuilder(getString(R.string.ERROR_DISCRIPTIOn)).apply {
				append("\n")
				append(getString(R.string.Error_code_message))
				append(" ")
				append(code)
				append("\n")
				when (code) {
					-3 -> append(error!!.message)
					-2 -> append(getString(R.string.error_desc_m2))
					-1 -> append(getString(R.string.error_desc_m1))
					in 200..299 -> append(getString(R.string.error_desc_200))
					in 300..399 -> append(getString(R.string.error_desc_300))
					in 400..499 -> append(getString(R.string.error_desc_400))
					in 500..599 -> append(getString(R.string.error_desc_500))
					else -> append(getString(R.string.error_desc_0))
				}
			}
		}
		else {
			popupSheet.title.text = StringBuilder(getString(R.string.Error_code_message)).apply {
				append(" ")
				append(code)
			}
			popupSheet.explanation.text = error!!.message
		}

		bottomSheetBehavior.halfExpandedRatio = 0.35f
		bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
	}

	override fun onSave() {
		binding.megaButton.text = getString(R.string.status_ready)
		binding.megaButton.setOnClickListener { presenter.needConversion() }
	}

	override fun onConvert() {
		binding.megaButton.text = getString(R.string.status_go)
	}

	override fun onDone() {
		binding.megaButton.text = getString(R.string.status_complete)
	}

	override fun loadingState(state: Boolean) {
		binding.loadingLayout.visibility = if (state) View.VISIBLE
		else View.GONE
	}
	//endregion

	companion object {
		@JvmStatic
		fun newInstance() = WindowPicture()
	}

}
