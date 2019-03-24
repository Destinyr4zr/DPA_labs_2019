package ru.mirea.lab1

object NumConverter {
    private val DIGIT_POWER = 6

    private val PowersAndCases = arrayOf(
        arrayOf("0", "", "", ""), // 1
        arrayOf("1", "тысяча ", "тысячи ", "тысяч "), // 2
        arrayOf("0", "миллион ", "миллиона ", "миллионов "), // 3
        arrayOf("0", "миллиард ", "миллиарда ", "миллиардов "), // 4
        arrayOf("0", "триллион ", "триллиона ", "триллионов "), // 5
        arrayOf("0", "квадриллион ", "квадриллиона ", "квадриллионов "), // 6
        arrayOf("0", "квинтиллион ", "квинтиллиона ", "квинтиллионов ")   // 7
    )

    private val DigitsAndCases = arrayOf(
        arrayOf("", "", "десять ", "", ""),
        arrayOf("один ", "одна ", "одиннадцать ", "десять ", "сто "),
        arrayOf("два ", "две ", "двенадцать ", "двадцать ", "двести "),
        arrayOf("три ", "три ", "тринадцать ", "тридцать ", "триста "),
        arrayOf("четыре ", "четыре ", "четырнадцать ", "сорок ", "четыреста "),
        arrayOf("пять ", "пять ", "пятнадцать ", "пятьдесят ", "пятьсот "),
        arrayOf("шесть ", "шесть ", "шестнадцать ", "шестьдесят ", "шестьсот "),
        arrayOf("семь ", "семь ", "семнадцать ", "семьдесят ", "семьсот "),
        arrayOf("восемь ", "восемь ", "восемнадцать ", "восемьдесят ", "восемьсот "),
        arrayOf("девять ", "девять ", "девятнадцать ", "девяносто ", "девятьсот ")
    )

    fun convertToText(sum: Int): String {
        var i: Int
        var mny: Int
        val result = StringBuilder()
        var divisor: Long
        var psum = sum

        val one = 1
        val four = 2
        val many = 3

        val hun = 4
        val dec = 3
        val dec2 = 2

        if (sum == 0) return "ноль "
        if (sum < 0) {
            result.append("минус ")
            psum = -psum
        }

        i = 0
        divisor = 1
        while (i < DIGIT_POWER) {
            divisor *= 1000
            i++
        }

        i = DIGIT_POWER - 1
        while (i >= 0) {
            divisor /= 1000
            mny = (psum / divisor).toInt()
            psum %= divisor.toInt()

            if (mny == 0) {
                if (i > 0) {
                    i--
                    continue
                }
                result.append(PowersAndCases[i][one])
            } else {
                if (mny >= 100) {
                    result.append(DigitsAndCases[mny / 100][hun])
                    mny %= 100
                }
                if (mny >= 20) {
                    result.append(DigitsAndCases[mny / 10][dec])
                    mny %= 10
                }
                if (mny >= 10) {
                    result.append(DigitsAndCases[mny - 10][dec2])
                } else {
                    if (mny >= 1) result.append(DigitsAndCases[mny][if ("0" == PowersAndCases[i][0]) 0 else 1])
                }
                when (mny) {
                    1 -> result.append(PowersAndCases[i][one])
                    2, 3, 4 -> result.append(PowersAndCases[i][four])
                    else -> result.append(PowersAndCases[i][many])
                }
            }
            i--
        }
        return result.toString()
    }

    fun convertToText(num: Double): String {
        return convertToText(num.toInt()) + "." + convertToText((num * 100 - num.toInt() * 100).toInt())
    }
}