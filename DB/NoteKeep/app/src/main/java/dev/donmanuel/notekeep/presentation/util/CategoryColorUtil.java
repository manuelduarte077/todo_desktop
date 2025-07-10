package dev.donmanuel.notekeep.presentation.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import dev.donmanuel.notekeep.R;

/**
 * Utilidad para manejar los colores de las categorías
 */
public class CategoryColorUtil {

    /**
     * Obtener el color correspondiente a una categoría
     *
     * @param context  Contexto de la aplicación
     * @param category Nombre de la categoría
     * @return El color como un entero
     */
    public static int getCategoryColor(Context context, String category) {
        if (category == null) {
            return ContextCompat.getColor(context, R.color.category_other);
        }

        switch (category) {
            case "Trabajo":
                return ContextCompat.getColor(context, R.color.category_work);
            case "Personal":
                return ContextCompat.getColor(context, R.color.category_personal);
            case "Estudio":
                return ContextCompat.getColor(context, R.color.category_study);
            default:
                return ContextCompat.getColor(context, R.color.category_other);
        }
    }

    /**
     * Aplicar el color de categoría a un TextView (badge)
     *
     * @param context  Contexto de la aplicación
     * @param textView TextView al que aplicar el color
     * @param category Nombre de la categoría
     */
    public static void applyCategoryColor(Context context, TextView textView, String category) {
        int color = getCategoryColor(context, category);
        textView.setBackgroundTintList(ColorStateList.valueOf(color));
    }
}
