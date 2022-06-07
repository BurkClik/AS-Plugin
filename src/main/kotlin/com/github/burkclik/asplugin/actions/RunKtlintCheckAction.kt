package com.github.burkclik.asplugin.actions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.terminal.TerminalView
import java.io.IOException

class RunKtlintCheckAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {

        // terminal
        // TODO: Burada newInstance oluşturmadan var olan terminalde çalıştırabilir miyiz?
        val terminalView = e.project?.let { TerminalView.getInstance(it) }
        val terminalTabTitle = "Ktlint"
        // endregion

        try {
            val terminalWidget = terminalView?.createLocalShellWidget(e.project?.basePath, terminalTabTitle)
            terminalWidget?.executeCommand(checkKtlintCommand)
            showNotification(e.project, "Running check ktlint")
        } catch (err: IOException) {
            err.printStackTrace()
        }
    }

    private fun showNotification(project: Project?, message: String) {
        NotificationGroupManager.getInstance().getNotificationGroup("Custom Notification Group")
            .createNotification(message, NotificationType.ERROR)
            .notify(project)
    }

    companion object {
        private const val checkKtlintCommand = "./gradlew ktlintCheck --continue"
    }
}