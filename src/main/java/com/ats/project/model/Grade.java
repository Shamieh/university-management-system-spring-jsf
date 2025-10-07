package com.ats.project.model;

import lombok.Getter;

@Getter
public enum Grade {

        A (90, 100, 4.0),
        B (80, 89, 3.0),
        C (70, 79, 2.0),
        D(60, 69, 1.0),
        F(0,59, 0.5);

        private final int min;
        private final int max;
        private final double points;

        Grade(int min, int max, double points){
            this.min = min;
            this.max = max;
            this.points = points;
        }


        public static Grade getGradeByMark(int mark) {
            for (Grade grade : Grade.values()) {
                if(mark >= grade.min && mark <= grade.max){
                    return grade;
                }
            }
            throw new IllegalArgumentException("Invalid mark");
        }
}
