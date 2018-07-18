package com.company.servotrajectorygui

import javafx.event.EventHandler
import javafx.stage.Stage
import tornadofx.*
import kotlin.concurrent.thread

class Gui : App(MainView::class, Style::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isResizable = false
    }
}

class MainView : View() {
    override val root = vbox {
        title = "Servo Trajectory Gui"
        addClass(Style.wrapper)
        hbox {
            addClass(Style.wrapper)
            button {
                text = "Reset servo"
                action {
                    link.setServoAngle(0)
                }
            }
            button {
                text = "Run trajectory"
                action {
                    thread {
                        trajectory.distancePoints.iterator().forEach {
                            link.setServoAngle(it)
                            Thread.sleep(1)
                        }
                    }
                }
            }
        }
        vbox {
            addClass(Style.wrapper)
            vbox {
                val distanceLabel = label("Distance: 90")
                slider(0, 180, 90) {
                    minorTickCount = 15
                    majorTickUnit = 15.0
                    isShowTickMarks = true
                    isShowTickLabels = true
                    valueProperty().addListener { _, _, newValue ->
                        distanceLabel.text = "Distance: ${newValue.toInt()}"
                    }
                    onMouseReleased = EventHandler {
                        setDistance(value.toInt())
                    }
                }
            }
            vbox {
                val velocityLabel = label("Max Velocity: 0.5")
                slider(0, 1, 0.5) {
                    minorTickCount = 10
                    majorTickUnit = 0.1
                    isShowTickMarks = true
                    isShowTickLabels = true
                    valueProperty().addListener { _, _, newValue ->
                        velocityLabel.text = "Max Velocity: ${newValue.toDouble()}"
                    }
                    onMouseReleased = EventHandler {
                        setVelocity(value)
                    }
                }
            }
            vbox {
                val accelerationLabel = label("Max Acceleration: 0.005")
                slider(0, 0.01, 0.005) {
                    minorTickCount = 10
                    majorTickUnit = 0.001
                    isShowTickMarks = true
                    isShowTickLabels = true
                    valueProperty().addListener { _, _, newValue ->
                        accelerationLabel.text = "Max Acceleration: ${newValue.toDouble()}"
                    }
                    onMouseReleased = EventHandler {
                        setAcceleration(value)
                    }
                }
            }
        }
    }
}

class Style : Stylesheet() {
    companion object {
        val wrapper by cssclass()
    }
    init {
        root {
            padding = box(5.mm)
        }
        slider {
            prefWidth = 150.mm
        }
        wrapper {
            spacing = 3.mm
        }
    }
}
