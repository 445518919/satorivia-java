package com.satoriviacafe.quartz.util;

import com.satoriviacafe.common.constant.Constants;
import com.satoriviacafe.common.constant.ScheduleConstants;
import com.satoriviacafe.common.utils.ExceptionUtil;
import com.satoriviacafe.common.utils.VStringUtils;
import com.satoriviacafe.common.utils.bean.BeanUtils;
import com.satoriviacafe.common.utils.spring.SpringUtils;
import com.satoriviacafe.quartz.domain.SysJob;
import com.satoriviacafe.quartz.domain.SysJobLog;
import com.satoriviacafe.quartz.service.ISysJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 抽象quartz调用 (适配虚拟线程，使用 ScopedValue 替换 ThreadLocal)
 *
 * @author satoriviacafe
 */
@SuppressWarnings({"Since15", "preview", "unused"})
public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 虚拟线程安全的作用域值，替代 ThreadLocal
     */
    private static final ScopedValue<Date> START_TIME = ScopedValue.newInstance();

    @Override
    public void execute(JobExecutionContext context) {
        SysJob sysJob = new SysJob();
        BeanUtils.copyBeanProp(sysJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        ScopedValue.where(START_TIME, new Date()).run(() -> {
            try {
                before(context, sysJob);
                doExecute(context, sysJob);
                after(context, sysJob, null);
            } catch (Exception e) {
                log.error("任务执行异常  - ：", e);
                after(context, sysJob, e);
            }
        });
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void before(JobExecutionContext context, SysJob sysJob) {
        // 不需要set，已在 ScopedValue.where 设置
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     */
    protected void after(JobExecutionContext context, SysJob sysJob, Exception e) {
        Date startTime = START_TIME.get();

        final SysJobLog sysJobLog = new SysJobLog();
        sysJobLog.setJobName(sysJob.getJobName());
        sysJobLog.setJobGroup(sysJob.getJobGroup());
        sysJobLog.setInvokeTarget(sysJob.getInvokeTarget());
        sysJobLog.setStartTime(startTime);
        sysJobLog.setStopTime(new Date());
        long runMs = sysJobLog.getStopTime().getTime() - sysJobLog.getStartTime().getTime();
        sysJobLog.setJobMessage("%s 总共耗时：%d毫秒".formatted(sysJobLog.getJobName(), runMs));
        if (e != null) {
            sysJobLog.setStatus(Constants.FAIL);
            String errorMsg = VStringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            sysJobLog.setExceptionInfo(errorMsg);
        } else {
            sysJobLog.setStatus(Constants.SUCCESS);
        }

        // 写入数据库当中
        SpringUtils.getBean(ISysJobLogService.class).addJobLog(sysJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param sysJob  系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, SysJob sysJob) throws Exception;
}
