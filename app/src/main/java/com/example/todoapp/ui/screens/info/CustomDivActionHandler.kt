package com.example.todoapp.ui.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class CustomDivActionHandler(
    private val navigateUp: () -> Unit
) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url =
            action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == NAV_URL_SCHEME && handleCustomAction(url, view.view.context)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleCustomAction(action: Uri, context: Context): Boolean {
        return when (action.host) {
            "redirect" -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(action.query))
                context.startActivity(intent)
                true
            }

            "navigation" -> {
                navigateUp()
                true
            }

            else -> false
        }
    }

    companion object {
        const val NAV_URL_SCHEME = "nav-action"
    }
}