package com.example.mazdacompanionapp.ui.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

/**
 *Reciever for Widget
 */
class CompanionWidgedReciever : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = CompanionWidget()
}
