package com.example.examscanner.components.scan_exam.reslove_answers.resolve_conflicted_answers;

public enum Choice {
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
//    SIX(6),
//    SEVEN(7),
    NO_ANSWER(0)
    ;
    public final int value;
    Choice(int i) {
        this.value=i;
    }
}
