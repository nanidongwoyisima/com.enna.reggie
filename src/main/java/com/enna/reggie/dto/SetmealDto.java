package com.enna.reggie.dto;

import com.enna.reggie.pojo.Setmeal;
import com.enna.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
