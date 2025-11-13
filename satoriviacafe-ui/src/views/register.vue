<template>
    <div class="register">
        <el-form ref="sendEmailForm" :model="sendMailCodeForm" :rules="sendEmailRules" class="register-form">
            <h3 class="title">{{ title }}</h3>
            <el-form-item prop="email">
                <el-input v-model="sendMailCodeForm.email" type="text" auto-complete="off" placeholder="电子邮箱">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
                </el-input>
            </el-form-item>
            <el-form-item prop="code" v-if="captchaEnabled">
                <el-input
                    v-model="sendMailCodeForm.code"
                    auto-complete="off"
                    placeholder="输入图片中的结果"
                    style="width: 63%"
                    @keyup.enter.native="handleEmailCode"
                >
                    <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
                </el-input>
                <div class="register-code">
                    <img :src="codeUrl" @click="getCode" class="register-code-img"/>
                </div>
            </el-form-item>
            <el-form-item style="width:100%;">
                <el-button
                    :loading="loading"
                    size="medium"
                    type="primary"
                    style="width:100%;"
                    @click.native.prevent="handleEmailCode"
                    :disabled="countdown > 0"
                >
                    <span v-if="!loading && countdown === 0">发送邮箱验证码</span>
                    <span v-if="!loading && countdown > 0">重新发送 ({{ countdown }}s)</span>
                    <span v-if="loading">发 送 中...</span>
                </el-button>
            </el-form-item>
        </el-form>

        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form" :disabled="!emailCodeSent">
            <el-form-item prop="username">
                <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="账号">
                    <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
                </el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input
                    v-model="registerForm.password"
                    type="password"
                    auto-complete="off"
                    placeholder="密码"
                    @keyup.enter.native="handleRegister"
                >
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                </el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword">
                <el-input
                    v-model="registerForm.confirmPassword"
                    type="password"
                    auto-complete="off"
                    placeholder="确认密码"
                    @keyup.enter.native="handleRegister"
                >
                    <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
                </el-input>
            </el-form-item>
            <el-form-item prop="code">
                <el-input
                    v-model="registerForm.code"
                    auto-complete="off"
                    placeholder="输入邮箱验证码"
                    style="width: 100%"
                    @keyup.enter.native="handleEmailCode"
                >
                    <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
                </el-input>
            </el-form-item>
            <el-form-item style="width:100%;">
                <el-button
                    :loading="loading"
                    size="medium"
                    type="primary"
                    style="width:100%;"
                    @click.native.prevent="handleRegister"
                    :disabled="!emailCodeSent"
                >
                    <span v-if="!loading">注 册</span>
                    <span v-else>注 册 中...</span>
                </el-button>
                <div style="float: right;">
                    <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
                </div>
            </el-form-item>
        </el-form>
        <!--  底部  -->
        <div class="el-register-footer">
            <span>Copyright © 2018 - 2025 satoriviacafe.com All Rights Reserved.</span>
        </div>
    </div>
</template>

<script>
import {getCodeImg, sendMailCode, v1Register} from "@/api/login";

const PASSWORD_REGEX = /^[^<>"'|\\]+$/;

export default {
    name: "Register",
    data() {
        const equalToPassword = (rule, value, callback) => {
            if (this.registerForm.password!== value) {
                callback(new Error("两次输入的密码不一致"));
            } else {
                callback();
            }
        };
        return {
            title: process.env.VUE_APP_TITLE,
            codeUrl: "",
            registerForm: {
                username: "",
                password: "",
                confirmPassword: "",
                code: "",
                email: "",
            },
            sendMailCodeForm: {
                email: "",
                code: "",
                uuid: ""
            },
            registerRules: {
                username: [
                    { required: true, trigger: "blur", message: "请输入您的账号" },
                    { min: 2, max: 20, message: '用户账号长度必须介于 2 和 20 之间', trigger: 'blur' }
                ],
                password: [
                    { required: true, trigger: "blur", message: "请输入您的密码" },
                    { min: 5, max: 20, message: "用户密码长度必须介于 5 和 20 之间", trigger: "blur" },
                    { pattern: PASSWORD_REGEX, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
                ],
                confirmPassword: [
                    { required: true, trigger: "blur", message: "请再次输入您的密码" },
                    { validator: equalToPassword, trigger: "blur" }
                ],
                code: [{ required: true, trigger: "change", message: "请输入邮箱验证码" }]
            },
            sendEmailRules: {
                email: [
                    { required: true, trigger: "blur", message: "请输入您的邮箱" },
                    { type: "email", message: "请输入正确的邮箱地址", trigger: ["blur", "change"] }
                ],
                code: [{ required: true, trigger: "change", message: "请输入验证码" }]
            },
            loading: false,
            captchaEnabled: true,
            emailCodeSent: false,
            countdown: 0,
            countdownInterval: null
        };
    },
    created() {
        this.getCode();
    },
    methods: {
        validateForm(formRef, callback) {
            this.$refs[formRef].validate(valid => {
                if (valid) {
                    callback();
                }
            });
        },
        handleApiError(errorMessage) {
            this.$message.error(errorMessage);
            if (this.captchaEnabled) {
                this.getCode();
            }
            this.loading = false;
        },
        async handleEmailCode() {
            this.validateForm('sendEmailForm', async () => {
                this.loading = true;
                try {
                    const res = await sendMailCode({
                        email: this.sendMailCodeForm.email,
                        code: this.sendMailCodeForm.code,
                        uuid: this.sendMailCodeForm.uuid
                    });
                    if (res.code === 200) {
                        this.registerForm.email = this.sendMailCodeForm.email;
                        this.$message.success("图片验证码验证成功，请继续完成注册！");
                        this.emailCodeSent = true;
                        this.startCountdown();
                    } else {
                        this.handleApiError("验证码错误，请重新输入！");
                    }
                } catch (e) {
                    this.handleApiError("图片验证码验证失败，请稍后重试！" + JSON.stringify(e));
                } finally {
                    this.loading = false;
                }
            });
        },
        getCode() {
            getCodeImg().then(res => {
                this.captchaEnabled = res.captchaEnabled === undefined? true : res.captchaEnabled;
                if (this.captchaEnabled) {
                    this.codeUrl = "data:image/gif;base64," + res.img;
                    this.sendMailCodeForm.uuid = res.uuid;
                }
            });
        },
        handleRegister() {
            this.validateForm('registerFormRef', () => {
                this.loading = true;
                v1Register({
                    username: this.registerForm.username,
                    password: this.registerForm.password,
                    code: this.registerForm.code,
                    email: this.registerForm.email
                })
                    .then(res => {
                        const username = this.registerForm.username;
                        this.$alert(`<font color='red'>恭喜你，您的账号 ${username} 注册成功！</font>`, '系统提示', {
                            dangerouslyUseHTMLString: true,
                            type:'success'
                        })
                            .then(() => {
                                this.$router.push("/login");
                            });
                    })
                    .catch(() => {
                        this.handleApiError("注册失败，请稍后重试！");
                    });
            });
        },
        startCountdown() {
            this.countdown = 60;
            this.countdownInterval = setInterval(() => {
                if (this.countdown > 0) {
                    this.countdown--;
                } else {
                    clearInterval(this.countdownInterval);
                }
            }, 1000);
        }
    }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.register {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-image: url("../assets/images/login-background.jpg");
    background-size: cover;
    background-position: center;
    padding: 20px;
    box-sizing: border-box;

    @media (max-width: 480px) {
        .register-form {
            width: 100%;
        }
    }
}

.title {
    margin: 0 auto 30px auto;
    text-align: center;
    color: #4a4a4a;
    font-size: 24px;
    font-weight: bold;
}

.register-form {
    border-radius: 8px;
    background: rgba(255, 255, 255, 0.9);
    width: 400px;
    padding: 30px 25px;
    margin-bottom: 20px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
    transition: transform 0.3s ease, box-shadow 0.3s ease;

    &:hover {
        transform: translateY(-5px);
        box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    }

    .el-input {
        height: 40px;

        input {
            height: 40px;
        }
    }

    .input-icon {
        height: 40px;
        width: 16px;
        margin-left: 4px;
    }
}

.register-code {
    width: 35%;
    height: 40px;
    float: right;

    img {
        cursor: pointer;
        vertical-align: middle;
        border-radius: 4px;
        border: 1px solid #dcdcdc;
    }
}

.el-button {
    border-radius: 20px;
    font-size: 16px;
}

.el-register-footer {
    height: 40px;
    line-height: 40px;
    position: fixed;
    bottom: 0;
    width: 100%;
    text-align: center;
    color: #fff;
    font-family: Arial, sans-serif;
    font-size: 12px;
    letter-spacing: 1px;
    background: rgba(0, 0, 0, 0.5);
}

.register-code-img {
    height: 40px;
    border-radius: 4px;
}
</style>
