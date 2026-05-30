package com.tgfcodes.upfile.application.usecase;

import com.tgfcodes.upfile.application.input.LoginInput;
import com.tgfcodes.upfile.application.output.LoginOutput;

public interface LoginUseCase {

    LoginOutput execute(LoginInput loginInput);
}