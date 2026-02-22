package com.example.myapplication.data

object QuotesProvider {

    // ✅ Citations générales (dashboard)
    private val generalQuotesFr = listOf(
        "Croyez en vous et vous êtes à mi-chemin.",
        "Votre seule limite, c'est votre esprit.",
        "L'action est la clé fondamentale de tout succès.",
        "Ne vous arrêtez pas avant d'être fier.",
        "Les routes difficiles mènent souvent à de belles destinations.",
        "Faites de chaque jour votre chef-d'œuvre.",
        "Concentrez-vous sur la productivité, pas sur l'agitation."
    )

    private val generalQuotesEn = listOf(
        "Believe you can and you're halfway there.",
        "Your only limit is your mind.",
        "Action is the foundational key to all success.",
        "Don't stop until you're proud.",
        "Difficult roads often lead to beautiful destinations.",
        "Make each day your masterpiece.",
        "Focus on being productive instead of busy."
    )

    // ✅ Citations par humeur — Français
    private val moodQuotesFr = mapOf(
        "😊" to listOf(
            "Le bonheur est contagieux, partagez-le !",
            "Votre sourire est votre plus beau vêtement.",
            "Chaque journée heureuse est un cadeau précieux.",
            "La joie est dans les petites choses."
        ),
        "😐" to listOf(
            "Même les jours ordinaires ont leur beauté.",
            "La constance est la force des grands.",
            "Un jour neutre est une page blanche à écrire.",
            "L'équilibre est une forme de sagesse."
        ),
        "😔" to listOf(
            "Après la pluie vient le beau temps.",
            "Chaque fin est un nouveau commencement.",
            "La tristesse prouve que vous aimez profondément.",
            "Demain sera meilleur — donnez-lui une chance."
        ),
        "😡" to listOf(
            "Respirez. Cette tempête aussi passera.",
            "La colère est une énergie — redirigez-la.",
            "La paix intérieure est votre vrai pouvoir.",
            "Choisissez vos batailles avec sagesse."
        ),
        "😴" to listOf(
            "Le repos est aussi productif que le travail.",
            "Recharger ses batteries, c'est avancer plus loin.",
            "Même les champions ont besoin de récupération.",
            "Prenez soin de vous — vous le méritez."
        ),
        "🤩" to listOf(
            "Votre enthousiasme déplace des montagnes !",
            "Cette énergie est un don — utilisez-la bien.",
            "Les grandes choses commencent par une étincelle.",
            "Gardez cette flamme allumée !"
        ),
        "😰" to listOf(
            "Respirez. Un pas à la fois.",
            "L'anxiété ment souvent sur l'avenir.",
            "Vous avez surmonté 100% de vos mauvaises journées.",
            "La tempête finit toujours par se calmer."
        )
    )

    // ✅ Citations par humeur — Anglais
    private val moodQuotesEn = mapOf(
        "😊" to listOf(
            "Happiness is contagious — spread it!",
            "Your smile is your best outfit.",
            "Every happy day is a precious gift.",
            "Joy is found in the little things."
        ),
        "😐" to listOf(
            "Even ordinary days have their beauty.",
            "Consistency is the strength of the great.",
            "A neutral day is a blank page to write.",
            "Balance is a form of wisdom."
        ),
        "😔" to listOf(
            "After rain comes sunshine.",
            "Every ending is a new beginning.",
            "Sadness proves you love deeply.",
            "Tomorrow will be better — give it a chance."
        ),
        "😡" to listOf(
            "Breathe. This storm will pass too.",
            "Anger is energy — redirect it.",
            "Inner peace is your true power.",
            "Choose your battles wisely."
        ),
        "😴" to listOf(
            "Rest is as productive as work.",
            "Recharging means going further.",
            "Even champions need recovery.",
            "Take care of yourself — you deserve it."
        ),
        "🤩" to listOf(
            "Your enthusiasm moves mountains!",
            "This energy is a gift — use it well.",
            "Great things start with a spark.",
            "Keep that flame burning!"
        ),
        "😰" to listOf(
            "Breathe. One step at a time.",
            "Anxiety often lies about the future.",
            "You've survived 100% of your bad days.",
            "The storm always calms eventually."
        )
    )

    // ✅ Citation générale aléatoire
    fun getRandomQuote(): String {
        return if (LanguageManager.isFrench())
            generalQuotesFr.random()
        else
            generalQuotesEn.random()
    }

    // ✅ Citation selon l'humeur sélectionnée
    fun getQuoteForMood(emoji: String): String {
        val map = if (LanguageManager.isFrench()) moodQuotesFr else moodQuotesEn
        return map[emoji]?.random() ?: getRandomQuote()
    }
}