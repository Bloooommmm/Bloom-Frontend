package id.ac.binus.bloomapp.data.local

import id.ac.binus.bloomapp.data.model.Lesson
import id.ac.binus.bloomapp.data.model.Question
import id.ac.binus.bloomapp.data.model.Unit

object LocalLearningData {
    val units: List<Unit> = listOf(
        Unit(
            id = "UNI01",
            title = "Body Changes & Puberty Basics",
            lessons = listOf(
                Lesson(
                    id = "LES01",
                    title = "What is Puberty?",
                    material = """
                        Puberty is a development phase from childhood to sexual maturity. Generally, female usually starts puberty between the ages of 10 - 14. In the other hand, male usually starts puberty between the ages of 12 - 16.
 
                        In puberty phase, adolescent will be changed bodily. This bodily changes due to change of hormon. In puberty phase, male and female will be taller.
                         
                        In some cases, this puberty phase starts prematurely. This is when female starts puberty before 8 years old, and male before 9 years old.
                         
                        Sometimes, children get confused about what’s happening in their body. Because of that, puberty education is important.
                        
                        Sign puberty of female
                        - Breast development begins
                        - Body hair begins to grow in new areas, including the underarm, legs, and pubic area
                        - Menstruation begins
                        - Voice matures
                         
                        Sign puberty of male
                        - Penis and testicles begin to grow
                        - Wet dream
                        - Body hair begins to grow in new areas, including the underarm, legs, and pubic area
                        - Voice deepens
                         
                        Source: Halodoc.com
                    """.trimIndent(),
                    questions = listOf(
                        Question(
                            id = "QUE001",
                            text = "What is the primary definition of puberty?",
                            options = mapOf(
                                "A" to "A stage of cognitive development in early childhood",
                                "B" to "The developmental phase transitioning from childhood to sexual maturity",
                                "C" to "The final stage of physical growth in adulthood",
                                "D" to "A temporary condition caused by sleep changes"
                            ),
                            correctOptionId = "B",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE002",
                            text = "Which of the following is a specific sign of puberty in females?",
                            options = mapOf(
                                "A" to "Voice deepening significantly",
                                "B" to "Growth of testicles",
                                "C" to "Onset of menstruation",
                                "D" to "Facial hair growth"
                            ),
                            correctOptionId = "C",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE003",
                            text = "What is the typical age range for males to start puberty?",
                            options = mapOf(
                                "A" to "8 to 10 years old",
                                "B" to "10 to 12 years old",
                                "C" to "12 to 16 years old",
                                "D" to "14 to 18 years old"
                            ),
                            correctOptionId = "C",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE004",
                            text = "Puberty in females is classified as precocious (premature) if it starts before what age?",
                            options = mapOf(
                                "A" to "8 years old",
                                "B" to "9 years old",
                                "C" to "10 years old",
                                "D" to "11 years old"
                            ),
                            correctOptionId = "A",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE005",
                            text = "What is the main biological trigger driving bodily changes during puberty?",
                            options = mapOf(
                                "A" to "Weather and climate shifts",
                                "B" to "Hormonal changes",
                                "C" to "Dietary changes alone",
                                "D" to "Increased daily exercise"
                            ),
                            correctOptionId = "B",
                            explanation = "-"
                        ),
                    )
                ),
                Lesson(
                    id = "LES01",
                    title = "What is Puberty?",
                    material = """
                        1.      Change in testicular volume
                        According to the Indonesian Pediatric Society (IDAI), the initial sign of puberty in boys is not the growth of a mustache, pubic hair, wet dreams, or an Adam's apple, but an increase in testicular volume. Although the exact timing varies for every individual, a boy who shows no increase in testicular volume by age 14 is considered to have delayed puberty. Conversely, if secondary sex characteristics appear before age 9, it is classified as precocious puberty  
                        2.      Experiencing wet dreams
                        According to the Indonesian Pediatric Society (IDAI), wet dreams is a sign that process spermatogenesis is activated. Wet dreams is ejaculation that occurs during sleep, when testosterone levels rise significantly.
                        3.      Voice Changes
                        During puberty, adolescent boys experience voice changes where their pitch deepens often referred to as a "voice break." This transition usually occurs between the ages of 11 and 15, progressing gradually over several months, often without them noticing.
                        4.      Height Increase (Growth Spurt)
                        Adolescent boys undergo a growth spurt during puberty. Peak growth velocity can reach up to 10 cm per year, with an overall total height gain of 25–30 cm throughout puberty. On average, the total height gain during puberty is greater in males than in females.
                        5.      Appearance of Acne
                        Acne breakouts are a common sign of pubertal development, though not all adolescent boys experience them. To help manage skin health, parents can encourage teens to wash their face regularly and maintain a balanced diet.
                        6.      Growth of Pubic and Body Hair
                        Another key marker of pubertal progression is the development of fine hair in the pubic and underarm areas. This somatic change is experienced by both adolescent males and females.
                        7.      Muscle Mass Growth
                        While puberty in females involves an increase in body fat percentage, puberty in males is characterized by a significant increase in muscle mass. Additionally, the chest broadens, taking on a more mature, adult male structure.
                         
                        Source: Halodoc.com
                    """.trimIndent(),
                    questions = listOf(
                        Question(
                            id = "QUE006",
                            text = "According to the Indonesian Pediatric Society (IDAI), what is the true initial sign of puberty in adolescent males?",
                            options = mapOf(
                                "A" to "Growth of a mustache",
                                "B" to "Deepening of the voice",
                                "C" to "Increase in testicular volume",
                                "D" to "First occurrence of a wet dream"
                            ),
                            correctOptionId = "C",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE007",
                            text = "A male is considered to have delayed puberty if he shows no increase in testicular volume by what age?",
                            options = mapOf(
                                "A" to "12 years old",
                                "B" to "13 years old",
                                "C" to "14 years old",
                                "D" to "15 years old"
                            ),
                            correctOptionId = "C",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE008",
                            text = "Male pubertal development is classified as precocious (premature) if secondary sexual characteristics appear before what age?",
                            options = mapOf(
                                "A" to "8 years old",
                                "B" to "9 years old",
                                "C" to "10 years old",
                                "D" to "11 years old"
                            ),
                            correctOptionId = "B",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE009",
                            text = "What biological process does the occurrence of wet dreams (nocturnal emissions) directly indicate?",
                            options = mapOf(
                                "A" to "The initiation of the linear growth spurt",
                                "B" to "The onset of active spermatogenesis",
                                "C" to "The completion of voice changes",
                                "D" to "The end of muscle mass growth"
                            ),
                            correctOptionId = "B",
                            explanation = "-"
                        ),
                        Question(
                            id = "QUE010",
                            text = "Within what age range do adolescent males typically experience voice changes (\"voice breaks\")?",
                            options = mapOf(
                                "A" to "8 to 10 years old",
                                "B" to "11 to 15 years old",
                                "C" to "16 to 18 years old",
                                "D" to "T18 to 20 years old"
                            ),
                            correctOptionId = "B",
                            explanation = "-"
                        ),
                    )
                )
            )
        )
    )
}