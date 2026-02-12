package com.kxtdev.bukkydatasup.common.enums

import com.kxtdev.bukkydatasup.ui.design.PoshImage


enum class ExamType(val id: Int, val title: String, val icon: Int) {
    WAEC(3, "WAEC", PoshImage.Waec),
    NECO(4, "NECO", PoshImage.Neco),
    NABTEB(5, "NABTEB", PoshImage.Nabteb),
    JAMB_UTME(6, "JAMB_UTME", PoshImage.Jamb),
    JAMB_DE(7, "JAMB_DE", PoshImage.Jamb);

    companion object {
        fun asPair(): List<Pair<String, Int>> {
            return ExamType.entries
                .filterNot { it == JAMB_DE || it == JAMB_UTME || it == NABTEB }
                .toTypedArray()
                .mapIndexed { _, exam -> Pair(exam.title, exam.icon) }
        }
        fun getByTitle(title: String): ExamType? {
            return entries
                .filterNot { it == JAMB_DE || it == JAMB_UTME || it == NABTEB }
                .toTypedArray()
                .find { it.title.lowercase() == title.lowercase() }
        }
    }
}