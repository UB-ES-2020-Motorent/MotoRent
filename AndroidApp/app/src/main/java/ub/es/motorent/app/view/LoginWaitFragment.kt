package ub.es.motorent.app.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import ub.es.motorent.R

class LoginWaitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login_wait, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewAdjuster.adjustView(view.findViewById(R.id.loading_tv))

        val loginProgress : ProgressBar = view.findViewById(R.id.login_progress)
        ViewAdjuster.adjustView(loginProgress)

        loginProgress.isIndeterminate = true
    }

}