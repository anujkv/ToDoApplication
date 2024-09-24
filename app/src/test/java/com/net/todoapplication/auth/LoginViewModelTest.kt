package com.net.todoapplication.auth

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.net.todoapplication.viewmodel.auth.LoginViewModel
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import javax.xml.validation.Validator
import com.google.android.gms.tasks.OnCompleteListener as OnCompleteListener1

class LoginViewModelTest {


    //Rules for use Mokito
    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    //Rules for test LiveData
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Mock firebaseAuth and viewModel
    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock private lateinit var authResult: Task<AuthResult>

    private lateinit var viewModel: LoginViewModel

    private lateinit var validator: Validator

    @Before
    fun setup() {
        viewModel = LoginViewModel(firebaseAuth)

    }

    @Test
    fun login_success_AuthResult(): Unit = run {
        // Mock the Firebase authentication result
        // Arrange
        val email = "anujkrvaish@gmail.com"
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        // Mock the FirebaseAuth and Task behavior
        whenever(authResult.isSuccessful).thenReturn(true)
        whenever(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResult)

        // Act
        viewModel.login(email, password, onResult)

        // Capture the OnCompleteListener
        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult) // Simulate task completion
        }

        // Assert
        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Sign-in Successful")
        }

    }

    @Test
    fun login_failed_AuthResult(): Unit = run {
        // Mock the Firebase authentication result
        val email = "anujkrvaish@gmail.com"
        val password = "anuj0122"
        val onResult: (String) -> Unit = mock()
        val errorMessage = "Invalid credentials"

        whenever(authResult.isSuccessful).thenReturn(false)
        whenever(authResult.exception).thenReturn(Exception(errorMessage))
        whenever(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResult)

        viewModel.login(email, password, onResult)

        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult) // Simulate task completion
        }

        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Authentication Failed: $errorMessage" || firstValue == "Sign-in Failed")
        }
    }

    @Test
    fun login_failed_AuthResult_null(): Unit = run{
        // Mock the Firebase authentication result
        val email = ""
        val password = ""
        val onResult: (String) -> Unit = mock()

        viewModel.login(email,password, onResult)

        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Authentication Failed: Email & password cannot be empty")
        }

    }


    @Test
    fun login_withEmptyEmail_shouldFail() {
        // Arrange
        val email = ""  // Empty email
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        // Act
        viewModel.login(email,password, onResult)

        // Assert
        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Authentication Failed: Email cannot be empty")
        }

    }

    @Test
    fun login_withEmptyPassword_shouldFail() {
        // Arrange
        val email = "anujkrvaish@gmail.com"
        val password = ""  // Empty password
        val onResult: (String) -> Unit = mock()

        // Act
        viewModel.login(email, password, onResult)

        // Assert
        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Authentication Failed: Password cannot be empty")
        }
    }


    /*@Test
    fun login_withInvalidEmailFormat_shouldFail(): Unit = run {
        // Arrange
        val email = "invalid-email-format"  // Invalid email format
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        whenever(authResult.isSuccessful).thenReturn(false)
        whenever(authResult.exception).thenReturn(Exception("The email address is badly formatted."))
        whenever(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResult)

        // Act

        viewModel.login(email, password, onResult)

        // Capture the OnCompleteListener
        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult)
        }

        // Assert
        argumentCaptor<String>().apply {
            verify(onResult).invoke(capture())
            assert(firstValue == "Authentication Failed: The email address is badly formatted.")
        }
    }*/

    /*@Test
    fun login_onResultCallbackIsAlwaysInvoked():Unit = run{
        // Arrange
        val email = "anujkrvaish@gmail.com"
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        // Act
        viewModel.login(email,password, onResult)

        whenever(authResult.isSuccessful).thenReturn(false)
        whenever(firebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(authResult)

        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult)
        }

        // Assert
        verify(onResult, times(1)).invoke(any())
    }*/

    @Test
    fun login_onResultCallbackIsInvokedOnce() : Unit = run {
        val email = "anujkrvaish@gmail.com"
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        whenever(authResult.isSuccessful).thenReturn(false)
        whenever(firebaseAuth.signInWithEmailAndPassword(email,password)).thenReturn(authResult)

        viewModel.login(email, password, onResult)

        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult)
        }

        verify(onResult, times(1)).invoke(any())

    }

    @Test
    fun authResult_updates_on_successful_login() : Unit = run {
        val email = "anujkrvaish@gmail.com"
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()

        whenever(authResult.isSuccessful).thenReturn(true)
        whenever(firebaseAuth.signInWithEmailAndPassword(email,password)).thenReturn(authResult)

        viewModel.login(email, password, onResult)

        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult)
        }

        assert(viewModel.authResult.value == "Sign-in Successful")
    }

    @Test
    fun authResult_updates_on_failed_login(): Unit = run {
        val email = "anujkrvaish@gmail.com"
        val password = "anuj012"
        val onResult: (String) -> Unit = mock()
        val errorMessage = "Invalid credentials"

        whenever(authResult.isSuccessful).thenReturn(false)
        whenever(authResult.exception).thenReturn(Exception(errorMessage))
        whenever(firebaseAuth.signInWithEmailAndPassword(email,password)).thenReturn(authResult)

        viewModel.login(email, password, onResult)

        argumentCaptor<OnCompleteListener1<AuthResult>>().apply {
            verify(authResult).addOnCompleteListener(capture())
            firstValue.onComplete(authResult)
        }

        assert(viewModel.authResult.value == "Authentication Failed: $errorMessage")
    }
}