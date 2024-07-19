package com.example.todoapp.ui.screens.info

import android.content.Context
import android.view.ContextThemeWrapper
import androidx.lifecycle.LifecycleOwner
import com.example.todoapp.R
import com.example.todoapp.utils.AssetReader
import com.yandex.div.DivDataTag
import com.yandex.div.core.Div2Context
import com.yandex.div.core.DivConfiguration
import com.yandex.div.core.expression.variables.DivVariableController
import com.yandex.div.core.view2.Div2View
import com.yandex.div.data.DivParsingEnvironment
import com.yandex.div.data.Variable
import com.yandex.div.json.ParsingErrorLogger
import com.yandex.div.picasso.PicassoDivImageLoader
import com.yandex.div2.DivData
import org.json.JSONObject


fun getDivView(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    customDivActionHandler: CustomDivActionHandler,
    variableController: DivVariableController,
    theme: Variable
): Div2View {
    val assertReader = AssetReader(context)

    val imageLoader = PicassoDivImageLoader(context)
    val configuration = DivConfiguration.Builder(imageLoader)
        .divVariableController(variableController)
        .actionHandler(customDivActionHandler)
        .build()


    variableController.putOrUpdate(theme)

    val divJson = assertReader.read("divData.json")
    val templatesJson = divJson.optJSONObject("templates")
    val cardJson = divJson.getJSONObject("card")

    val divContext = Div2Context(
        baseContext = ContextThemeWrapper(context, R.style.Theme_ToDoApp),
        configuration = configuration,
        lifecycleOwner = lifecycleOwner
    )

    val divView = Div2ViewFactory(divContext, templatesJson).createView(cardJson)
    return divView
}

class Div2ViewFactory(
    private val context: Div2Context,
    private val templatesJson: JSONObject? = null
) {
    private val environment = DivParsingEnvironment(ParsingErrorLogger.ASSERT).apply {
        if (templatesJson != null) parseTemplates(templatesJson)
    }

    fun createView(cardJson: JSONObject): Div2View {
        val divData = DivData(environment, cardJson)
        return Div2View(context).apply {
            setData(divData, DivDataTag("div2"))
        }
    }
}
