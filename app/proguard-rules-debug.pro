#-keep class de.robv.android.xposed.** { *; }
#
#-keep interface de.robv.android.xposed.IXposedHookLoadPackage { *; }
#-keep interface de.robv.android.xposed.IXposedHookZygoteInit { *; }
#-keep interface de.robv.android.xposed.IXposedHookInitPackageResources { *; }
#
#-keep class com.niki.breeno.MainHook { *;}
#
#-keep class io.ktor.** { *; }
#-dontwarn io.ktor.**
#
#-keep class com.aallam.openai.** { *; }
#-dontwarn com.aallam.openai.**
#
#-keepnames class kotlinx.coroutines.internal.* { *; }
#-dontwarn kotlinx.coroutines.flow.**
#-dontwarn kotlinx.coroutines.internal.**
#-dontwarn kotlinx.coroutines.debug.**
#
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
#
#-keep public class * extends android.view.View {
#    public <init>(android.content.Context);
#    public <init>(android.content.Context, android.util.AttributeSet);
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keep class * implements android.os.Parcelable {
#    public static final android.os.Parcelable$Creator CREATOR;
#}
#
#-keep class * implements java.io.Serializable {
#    static final long serialVersionUID;
#    private void writeObject(java.io.ObjectOutputStream);
#    private void readObject(java.io.ObjectInputStream);
#    java.lang.Object writeReplace();
#    java.lang.Object readResolve();
#}
#
#-dontwarn **