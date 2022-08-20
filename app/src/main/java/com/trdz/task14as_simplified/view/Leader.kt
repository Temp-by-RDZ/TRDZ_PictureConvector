package com.trdz.task14as_simplified.view

/**
 * Ответсвеенный за распределение задач и выдающий команды
 * Когда фрагменту требуется выполнить задачу или перейти на другой фрагмент
 * он обращается к лидеру и тот либо переводит в навигацию либо в исполнение задач
 */
interface Leader {
	fun getNavigation(): Navigation
	fun getExecutor(): Executor
}