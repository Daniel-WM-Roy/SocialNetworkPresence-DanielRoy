# Personal Review of Social Network Pressure Technical Assignment

This was a fun little task to tackle. Enjoyed getting to try out my ZIO knowledge some more.
Eager to get feedback, so I can improve my skills.

The only real logic in the solution is contained inside of `SocialMediaConnectionLogic` so only that has been tested,
the spec for which can be found in `SocialMediaConnectionLogicSpec`.

You could also provide tests for the decoding of the client response.

## Approach to the Problem

I saw the problem as essentially testing one's "style" of coding, so to speak. Do I write readable code?
Is the logic easy to follow? Do I adhere to FP principles? I kept these points in mind while writing.

I immediately set about specifying the domain of the data involved. I created the case classes (and JSON decoders) for the data to be received via the client.

Next, I created the client. With no actual API to query, I used the example data in the README as a basis to work from. I implemented the client trait to 
always return this data.

At first, I misconstrued the wording in the task, thinking that the objective was to create something "runnable" so it could be discussed in review.
That I did, specifying a Main class that could easily be run and the results printed to the console.

I later learned that a test suite was explicitly expected, which I added later. Sadly, I missed my opportunity to design the code by TDD!

## Improvements

* Resolve the DecodingError for an empty response from the client
  * In the case the Social Media Client returns an empty response, a DecodingError is being thrown, as the code is not prepared to handle an empty response