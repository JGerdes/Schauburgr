package com.jonasgerdes.schauburgr.usecase.home.guide.day_list;

import com.jonasgerdes.schauburgr.model.schauburg.entity.Screening;

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 21.04.2017
 */
public interface ScreeningSelectedListener {
    void onScreeningSelected(Screening screening);
}
