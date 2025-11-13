package com.satoriviacafe.web.controller.tool;

import com.satoriviacafe.common.core.controller.BaseController;
import com.satoriviacafe.common.core.domain.R;
import com.satoriviacafe.common.utils.VStringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger 用户测试方法
 *
 * @author satoriviacafe
 */
@Tag(name = "用户信息管理 (TestController)", description = "用于测试用户相关操作的API")
@RestController
@RequestMapping("/test/user")
public class TestController extends BaseController {

    private final static Map<Integer, UserEntity> users = new LinkedHashMap<>();

    static {
        users.put(1, new UserEntity(1, "admin", "admin123", "15888888888"));
        users.put(2, new UserEntity(2, "ry", "admin123", "15666666666"));
    }

    @Operation(summary = "获取用户列表", description = "查询所有用户的列表")
    @GetMapping("/list")
    public R<List<UserEntity>> userList() {
        List<UserEntity> userList = new ArrayList<>(users.values());
        return R.ok(userList);
    }

    @Operation(summary = "获取用户详细信息", description = "根据用户ID获取单个用户的详细信息")
    @GetMapping("/{userId}")
    public R<UserEntity> getUser(
            @Parameter(description = "用户ID", required = true, example = "1") @PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            return R.ok(users.get(userId));
        } else {
            return R.fail("用户不存在");
        }
    }

    @Operation(summary = "新增用户", description = "创建一个新用户。参数通过表单或请求体参数绑定到UserEntity对象。")
    // For POST with POJO without @RequestBody, springdoc-openapi typically infers parameters from UserEntity fields.
    // If you want to be very explicit for form data, you might need to list individual @Parameter annotations
    // or use @RequestBody with mediaType = "application/x-www-form-urlencoded" and define the schema.
    // For simplicity, relying on POJO field inference is common.
    @PostMapping("/save")
    public R<String> save(UserEntity user) { // Spring MVC will bind request params to UserEntity fields
        if (VStringUtils.isNull(user) || VStringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        users.put(user.getUserId(), user);
        return R.ok("用户新增成功");
    }

    @Operation(summary = "更新用户信息", description = "根据用户ID更新现有用户的信息")
    @PutMapping("/update")
    public R<String> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新的用户信息", required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(schema = @Schema(implementation = UserEntity.class)))
            @RequestBody UserEntity user) {
        if (VStringUtils.isNull(user) || VStringUtils.isNull(user.getUserId())) {
            return R.fail("用户ID不能为空");
        }
        if (users.isEmpty() || !users.containsKey(user.getUserId())) {
            return R.fail("用户不存在");
        }
        // users.remove(user.getUserId()); // Not strictly necessary if put overwrites
        users.put(user.getUserId(), user);
        return R.ok("用户更新成功");
    }

    @Operation(summary = "删除用户信息", description = "根据用户ID删除用户")
    @DeleteMapping("/{userId}")
    public R<String> delete(
            @Parameter(description = "要删除的用户ID", required = true, example = "1") @PathVariable Integer userId) {
        if (!users.isEmpty() && users.containsKey(userId)) {
            users.remove(userId);
            return R.ok("用户删除成功");
        } else {
            return R.fail("用户不存在");
        }
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "UserEntity", description = "用户实体对象")
class UserEntity {

    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer userId;

    @Schema(description = "用户名称", example = "john.doe")
    private String username;

    @Schema(description = "用户密码", example = "password123")
    private String password;

    @Schema(description = "用户手机", example = "13800138000")
    private String mobile;
}
