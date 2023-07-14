package algonquin.cst2335.bagg0051;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        // find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        // type in Password@10
        appCompatEditText.perform(replaceText("Password@10"), closeSoftKeyboard());
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // click the button
        materialButton.perform(click());
        // find the textview
        ViewInteraction textView = onView(withId(R.id.passwordText));
        // check the text
        textView.check(matches(withText("Your password meets the requirements")));
    }

    @Test
    public void testFindMissingUpperCase(){
        // find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        // type in pass@10
        appCompatEditText.perform(replaceText("pass@10"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // click the button
        materialButton.perform(click());
        // find the textview
        ViewInteraction textView = onView(withId(R.id.passwordText));
        // check the text
        textView.check(matches(withText("You shall not pass!")));
    }

    @Test
    public void testFindMissingLowerCase(){
        // find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        // type in PASS@10
        appCompatEditText.perform(replaceText("PASS@10"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // click the button
        materialButton.perform(click());
        // find the textview
        ViewInteraction textView = onView(withId(R.id.passwordText));
        // check the text
        textView.check(matches(withText("You shall not pass!")));
    }
    @Test
    public void testFindMissingNumber(){
        // find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        // type in PaSS@10
        appCompatEditText.perform(replaceText("PaSS@"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // click the button
        materialButton.perform(click());
        // find the textview
        ViewInteraction textView = onView(withId(R.id.passwordText));
        // check the text
        textView.check(matches(withText("You shall not pass!")));
    }
    @Test
    public void testFindMissingSymbol(){
        // find the view
        ViewInteraction appCompatEditText = onView( withId(R.id.passwordEditText));
        // type in Pass10
        appCompatEditText.perform(replaceText("Pass10"));
        // find the button
        ViewInteraction materialButton = onView(withId(R.id.loginButton));
        // click the button
        materialButton.perform(click());
        // find the textview
        ViewInteraction textView = onView(withId(R.id.passwordText));
        // check the text
        textView.check(matches(withText("You shall not pass!")));
    }
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
