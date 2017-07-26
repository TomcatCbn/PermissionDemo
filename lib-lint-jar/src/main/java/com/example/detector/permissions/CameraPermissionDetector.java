package com.example.detector.permissions;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaRecursiveElementVisitor;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by boning on 2017/7/26.
 */

public class CameraPermissionDetector extends Detector implements Detector.JavaPsiScanner {

    private static final String ID = "CameraPermissionMissing";
    private static final String ACTION_CAMERA = "MediaStore.ACTION_IMAGE_CAPTURE";
    private static final String ACTION_CAMERA2 = "android.provider.MediaStore.ACTION_IMAGE_CAPTURE";

    public static final Issue ISSUE =
            Issue.create(
                    ID,
                    "When use camera, you should check permission.",
                    "use PermissionUtil to check permissions",
                    Category.CORRECTNESS,
                    7,
                    Severity.WARNING,
                    new Implementation(CameraPermissionDetector.class,
                            EnumSet.of(Scope.JAVA_FILE))
            );


    @Override
    public List<Class<? extends PsiElement>> getApplicablePsiTypes() {
        return Collections.<Class<? extends PsiElement>>singletonList(PsiCallExpression.class);
    }

    @Override
    public JavaElementVisitor createPsiVisitor(final JavaContext context) {
        return new JavaElementVisitor() {
            @Override
            public void visitCallExpression(PsiCallExpression callExpression) {
                checkCallIntent(context, callExpression);
                super.visitCallExpression(callExpression);
            }
        };
    }

    private void checkCallIntent(JavaContext context, PsiCallExpression callExpression) {
        String text = callExpression.getText();
        if (text == null) {
            return;
        }

        boolean callCamera = text.contains(ACTION_CAMERA) || text.contains(ACTION_CAMERA2);
        if (callCamera) {
            PsiMethod method = PsiTreeUtil.getParentOfType(callExpression, PsiMethod.class, true);
            if (method != null) {
                MethodCallChecker visitor = new MethodCallChecker(callExpression);
                method.accept(visitor);
                boolean result = visitor.checksPermission();
                if (!result) {
                    context.report(ISSUE, callExpression, context.getLocation(callExpression), "访问相机，需要检查权限");
                }
            }
        }
    }

    static class MethodCallChecker extends JavaRecursiveElementVisitor {
        private boolean mChecksPermission;
        private boolean mDone;
        private final PsiElement mTarget;

        public MethodCallChecker(@NonNull PsiElement target) {
            mTarget = target;
        }

        @Override
        public void visitElement(PsiElement element) {
            if (!mDone) {
                super.visitElement(element);
            }
        }

        @Override
        public void visitMethodCallExpression(PsiMethodCallExpression node) {
            if (node == mTarget) {
                mDone = true;
            }

            String name = node.getMethodExpression().getReferenceName();
            if (name != null
                    && (name.startsWith("check") || name.startsWith("enforce"))
                    && name.endsWith("Permission")) {
                mChecksPermission = true;
                mDone = true;
            }
        }

        public boolean checksPermission() {
            return mChecksPermission;
        }
    }

}
