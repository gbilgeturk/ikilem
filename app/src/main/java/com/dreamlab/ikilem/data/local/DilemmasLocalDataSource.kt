package com.dreamlab.ikilem.data.local

import com.dreamlab.ikilem.data.model.Category
import com.dreamlab.ikilem.data.model.Dilemma

object DilemmasLocalDataSource {

    // Yeni havuz (30 ikilem)
    private val all = listOf(
        // Günlük
        Dilemma("G1", Category.GUNLUK, "Her gün 1 saat erken uyanmak", "Her gün 1 saat geç yatmak"),
        Dilemma("G2", Category.GUNLUK, "Sosyal medyasız 1 ay", "Şekersiz 1 ay"),
        Dilemma("G3", Category.GUNLUK, "Her gün evde kahvaltı", "Her gün dışarıda kahve"),
        Dilemma("G4", Category.GUNLUK, "Hep toplu taşımayla gezmek", "Hep yürüyerek gitmek"),
        Dilemma("G5", Category.GUNLUK, "Hafta içi 1 gün tam tatil", "Her gün 1 saat erken paydos"),

        // Aşk & İlişki
        Dilemma("A1", Category.ASK_ILISKI, "Uzun mesafe ilişki", "Aynı şehir ama yoğun iş"),
        Dilemma("A2", Category.ASK_ILISKI, "Doğum gününü unutmak", "Yıldönümünü yanlış hatırlamak"),
        Dilemma("A3", Category.ASK_ILISKI, "Partnerinle ortak hobi", "Partnerinle ayrı alan"),
        Dilemma("A4", Category.ASK_ILISKI, "İlk buluşmada sessizlik", "İlk buluşmada aşırı konuşmak"),
        Dilemma("A5", Category.ASK_ILISKI, "Aileyle tanışma erken", "Aileyle tanışma geç"),

        // Kariyer & Para
        Dilemma("K1", Category.KARIYER_PARA, "Yüksek maaş + sıkıcı iş", "Orta maaş + tutkunu iş"),
        Dilemma("K2", Category.KARIYER_PARA, "Tam uzaktan çalışma", "Hibrit ama ekip sıcak"),
        Dilemma("K3", Category.KARIYER_PARA, "Erken terfi, fazla sorumluluk", "Geç terfi, dengeli hayat"),
        Dilemma("K4", Category.KARIYER_PARA, "İsim yapmış şirket", "Küçük ama özgür start‑up"),
        Dilemma("K5", Category.KARIYER_PARA, "Primli belirsiz gelir", "Sabit ama düşük gelir"),

        // Macera & Risk
        Dilemma("M1", Category.MACERA, "Tek başına seyahat", "Kalabalık grupla seyahat"),
        Dilemma("M2", Category.MACERA, "Dağda kamp", "Deniz kıyısında kamp"),
        Dilemma("M3", Category.MACERA, "Paraşüt atlayışı", "Dalışla resif keşfi"),
        Dilemma("M4", Category.MACERA, "Haritasız şehir turu", "Planlı tur ama sıkı program"),
        Dilemma("M5", Category.MACERA, "Adrenalinsiz güvenli tatil", "Kısa ama çok riskli aktivite"),

        // Fantastik / Absürt
        Dilemma("F1", Category.FANTASTIK, "Görünmez olmak", "Zihin okumak"),
        Dilemma("F2", Category.FANTASTIK, "Zamanda geriye gitmek", "Zamanda ileriye bakmak"),
        Dilemma("F3", Category.FANTASTIK, "Uçabilmek", "Işınlanmak"),
        Dilemma("F4", Category.FANTASTIK, "Hayvanlarla konuşmak", "Her dili konuşmak"),
        Dilemma("F5", Category.FANTASTIK, "Her gün 25. saat", "Haftada 3 gün çalışma"),
        // Ek birkaç tane daha karışık
        Dilemma("X1", Category.GUNLUK, "Kahvaltıda hep tuzlu", "Kahvaltıda hep tatlı"),
        Dilemma("X2", Category.KARIYER_PARA, "Küçük şehirde ucuz yaşam", "Büyük şehirde pahalı yaşam"),
        Dilemma("X3", Category.ASK_ILISKI, "Sürprizleri sevmek", "Planlı ilerlemek"),
        Dilemma("X4", Category.MACERA, "Yüksek tempolu şehir", "Sakin sahil kasabası"),
        Dilemma("X5", Category.FANTASTIK, "Günde 4 saat uyku yetmesi", "Yemek yemeden doymak")
    )

    fun random(category: Category?): Dilemma =
        when (category) {
            null -> all.random()
            else -> all.filter { it.category == category }.ifEmpty { all }.random()
        }

    fun allCategoriesCount(): Map<Category, Int> =
        all.groupingBy { it.category }.eachCount()

    fun allCount(): Int = all.size
}
