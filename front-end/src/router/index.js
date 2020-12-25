import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import submission from '../components/submission.vue'
import main from '../components/main.vue'
import rate from '../components/rate.vue'
import studentHome from "@/components/studentHome";
Vue.use(VueRouter)
 
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

const routes = [{
		path: '/',
		name: 'Home',
		component: studentHome,
		children: [{
				path: '/questionBank',
				name: 'questionBank',
				component: () => import('../components/questionBank'),
				children: [{
						path: '/main',
						name: 'main',
						component: main
					},
					{
						path: '/submission',
						name: 'submission',
						component: submission
					},
					{
						path: '/rate',
						name: 'rate',
						component: rate
					}
				]
			},
			{
				path: '/contest',
				name: 'contest',
				component: () => import('../components/contest')
			}
		]
	},
	{
		path: '/answer/:id',
		name: 'answer',
		component: () => import( /* webpackChunkName: "about" */ '../views/answer.vue')
	},
	{
		path: '/adminMain',
		name: 'adminMain',
		component: () => import('../views/adminMain.vue'),
		children: [
			{
				path: '/adminMain/ContestControl',
				name: 'ContestControl',
				component: () => import('../components/createContest')
			},
			{
				path: '/adminMain/submissionRecord',
				name: 'submissionRecord',
				component: () => import('../components/AdminSubmissionRecord')
			}]
	},
	{
		path: '/contestCoding/:contestId',
		name: 'contestCoding',
		component: () => import('../components/contestCoding'),
		children: [{
			path: '/contestCoding/submissions',
			name: 'contestSubmissions',
			component: () => import('../components/submissionRecord')
		}]
	},

]

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

export default router
