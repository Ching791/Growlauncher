package com.rtsoft.growtopia;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gentz.launcher.App;

/**
 * AAP Bypasser dialog — lets users store a previously-whitelisted MAC address so the
 * engine reports it on the next reconnect, bypassing Growtopia's Advanced Account
 * Protection new-device check without touching any native packets.
 */
public class AapBypasserDialog {

    private static final int BG_PANEL  = 0xFF1A3A5C;
    private static final int BG_INPUT  = 0xFF0D2B45;
    private static final int COLOR_GOLD = 0xFFFFBB00;
    private static final int COLOR_GREEN = 0xFF00CC66;

    public static void show(Context context) {
        String savedMac = getSavedMac();

        // ── Root panel ────────────────────────────────────────────────────
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(BG_PANEL);
        int p = dp(context, 18);
        root.setPadding(p, p, p, p);

        // ── Title row: lock icon + "AAP Bypasser" ─────────────────────────
        LinearLayout titleRow = new LinearLayout(context);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setGravity(Gravity.CENTER_VERTICAL);
        titleRow.setLayoutParams(rowLp(context, 0, 0));

        TextView lockIcon = new TextView(context);
        lockIcon.setText("🔒 "); // 🔒
        lockIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        titleRow.addView(lockIcon);

        TextView title = new TextView(context);
        title.setText("AAP Bypasser");
        title.setTextColor(COLOR_GREEN);
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        title.setTypeface(null, Typeface.BOLD);
        titleRow.addView(title);
        root.addView(titleRow);

        // ── Divider ───────────────────────────────────────────────────────
        View divider = new View(context);
        LinearLayout.LayoutParams divLp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, dp(context, 1));
        divLp.topMargin = dp(context, 10);
        divLp.bottomMargin = dp(context, 10);
        divider.setLayoutParams(divLp);
        divider.setBackgroundColor(0x44FFFFFF);
        root.addView(divider);

        // ── Warning/info text ─────────────────────────────────────────────
        String redPart   = "Looks Like This Account Has Been Protected. ";
        String greenPart = "If You Have Saved The Whitelisted MAC Address, You Can Bypass "
                + "This Protection. ";
        String grayPart  = "If You Don't Have Saved Whitelisted MAC Address And You Don't "
                + "Have Access To The Referred Email On This Account, Then There Is "
                + "Nothing You Can Do...";

        SpannableString body = new SpannableString(redPart + greenPart + grayPart);
        body.setSpan(new ForegroundColorSpan(Color.RED),
                0, redPart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        body.setSpan(new ForegroundColorSpan(COLOR_GREEN),
                redPart.length(), redPart.length() + greenPart.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        body.setSpan(new ForegroundColorSpan(COLOR_GREEN),
                redPart.length() + greenPart.length(), body.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView info = new TextView(context);
        info.setText(body);
        info.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        root.addView(info);

        // ── "Input The Whitelisted MAC:" label ────────────────────────────
        TextView inputLabel = new TextView(context);
        inputLabel.setText("Input The Whitelisted MAC:");
        inputLabel.setTextColor(Color.WHITE);
        inputLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        LinearLayout.LayoutParams labelLp = rowLp(context, 14, 6);
        inputLabel.setLayoutParams(labelLp);
        root.addView(inputLabel);

        // ── MAC: row ──────────────────────────────────────────────────────
        LinearLayout macRow = new LinearLayout(context);
        macRow.setOrientation(LinearLayout.HORIZONTAL);
        macRow.setGravity(Gravity.CENTER_VERTICAL);
        macRow.setLayoutParams(rowLp(context, 0, 0));

        TextView macLabel = new TextView(context);
        macLabel.setText("MAC: ");
        macLabel.setTextColor(Color.WHITE);
        macLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        macLabel.setTypeface(null, Typeface.BOLD);
        macRow.addView(macLabel);

        EditText macInput = new EditText(context);
        macInput.setText(savedMac);
        macInput.setTextColor(Color.WHITE);
        macInput.setHintTextColor(0xFF666666);
        macInput.setHint("xx:xx:xx:xx:xx:xx");
        macInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        macInput.setBackgroundColor(BG_INPUT);
        int ep = dp(context, 8);
        macInput.setPadding(ep, ep / 2, ep, ep / 2);
        macInput.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        macRow.addView(macInput);
        root.addView(macRow);

        // ── Dialog ────────────────────────────────────────────────────────
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        dialog.setOnShowListener(d -> {
            Button save   = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            if (save != null) {
                save.setTextColor(Color.WHITE);
                save.setBackgroundColor(0xFF555555);
                save.setOnClickListener(v -> {
                    // Normalize: strip colons/hyphens so the engine always gets
                    // the 12-char no-colon format that get_macAddress() returns.
                    String mac = macInput.getText().toString().trim()
                            .replace(":", "").replace("-", "").toLowerCase();
                    if (mac.isEmpty()) {
                        Toast.makeText(context, "Please enter a MAC address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mac.length() != 12 || !mac.matches("[0-9a-f]+")) {
                        Toast.makeText(context,
                                "Enter a valid MAC (12 hex digits or xx:xx:xx:xx:xx:xx)",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    saveMac(mac);
                    Toast.makeText(context,
                            "MAC saved! Reconnect to apply.", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                });
            }
            if (cancel != null) {
                cancel.setTextColor(Color.BLACK);
                cancel.setBackgroundColor(COLOR_GOLD);
            }
        });

        dialog.show();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String getSavedMac() {
        try {
            SharedPreferences prefs =
                    App.f10088p.getSharedPreferences("launcher_data", Context.MODE_PRIVATE);
            String mac = prefs.getString("mac", null);
            if (mac == null || mac.length() != 12) return mac != null ? mac : "";
            // Display in colon-separated form for readability
            return mac.substring(0, 2) + ":" + mac.substring(2, 4) + ":" + mac.substring(4, 6)
                    + ":" + mac.substring(6, 8) + ":" + mac.substring(8, 10) + ":" + mac.substring(10, 12);
        } catch (Exception e) {
            return "";
        }
    }

    private static void saveMac(String mac) {
        try {
            SharedPreferences prefs =
                    App.f10088p.getSharedPreferences("launcher_data", Context.MODE_PRIVATE);
            prefs.edit().putString("mac", mac).apply();
        } catch (Exception ignored) {}
    }

    private static LinearLayout.LayoutParams rowLp(Context ctx, int topDp, int botDp) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = dp(ctx, topDp);
        lp.bottomMargin = dp(ctx, botDp);
        return lp;
    }

    private static int dp(Context ctx, int dp) {
        return Math.round(dp * ctx.getResources().getDisplayMetrics().density);
    }
}
