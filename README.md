## Intro

One of the things we do at Euromoney is to query semantic repositories to be able to extract and analyze the information present in the datasets.
We would like you to build a system that accepts questions in natural language and retrieves the answer for them from a semantic data source such as dbpedia.org.

- You should spend about 60 minutes on this, but feel free to take longer than that to prove good design skills and correctness of your answer.
- You can use as many java frameworks as you might find necessary for your solution.
- [JUnit](http://www.junit.org/) and [Mockito](http://mockito.org/), references have been added using [Maven](http://maven.apache.org/) build management software.

## Task requirements

- To be completed with an appropriate level of testing.
- It is suggested to use dbpedia.org as semantic repository, but feel free to use any other semantic data sources you might find handy.
- Reformat, refactor and rework the provided code in any way you see fit.
- Code must be supported by tests to be "done-done".


As a **user**
I want to be able to make questions to the system about someone's place of birth
by formulating the following question "What is the birth place of David Cameron?".
	
#### Acceptance criteria
	Input: "What is the birth place of David Cameron?"
	Output: "London, United Kingdom"

	Input: Where was David Cameron born?"
	Output: "London, United Kingdom"

---

Thanks for your time, we look forward to hearing from you!
