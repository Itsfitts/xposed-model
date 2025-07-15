-keep class de.robv.android.xposed.** { *; }

-keep interface de.robv.android.xposed.IXposedHookLoadPackage { *; }
-keep interface de.robv.android.xposed.IXposedHookZygoteInit { *; }
-keep interface de.robv.android.xposed.IXposedHookInitPackageResources { *; }

-keep class com.niki.xposed.MainHook { *;}

-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

-keepnames class kotlinx.coroutines.internal.* { *; }
-dontwarn kotlinx.coroutines.flow.**
-dontwarn kotlinx.coroutines.internal.**
-dontwarn kotlinx.coroutines.debug.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

-keep class * implements java.io.Serializable {
    static final long serialVersionUID;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Okio (OkHttp 的依赖)
-dontwarn okio.**
-keep class okio.** { *; }
-keep interface okio.** { *; }

# 保持所有数据类和模型类不被混淆，特别是 Retrofit 需要通过反射访问的类
# 替换 com.your.package.model 为你实际的 beans 包路径
-keep class com.niki.chat.beans.** { *; } # 保持你的 ChatCompletionRequest, Message 等数据类

# 对于 Kotlin 反射和泛型，通常也需要一些规则
-keep class kotlin.Metadata { *; }
-keep class kotlin.coroutines.Continuation
-keep class kotlin.coroutines.intrinsics.IntrinsicsKt
-keep class kotlin.jvm.internal.DefaultConstructorMarker { *; }

# 对于 suspend 函数的通用规则 (如果遇到问题可以添加)
-keep class * extends kotlin.coroutines.Continuation { *; }

-dontwarn **