package com.ecomap.ukraine.data.manager;

import com.ecomap.ukraine.models.Details;
import com.ecomap.ukraine.models.Problem;

import java.util.List;

/**
 * Created by Alexander on 27.07.2015.
 */
public interface RequestReceiver {

    void setAllProblemsRequestResult(List<Problem> problems);

    void setProblemDetailsRequestResult(Details details);
}
