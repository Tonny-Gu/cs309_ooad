import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import submission from '../components/submission.vue'
import main from '../components/main.vue'
import rate from '../components/rate.vue'
import studentHome from "@/components/studentHome";
Vue.use(VueRouter)

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
			},
			{
				path: '/forum',
				name: 'forum',
				component: () => import('../components/forum')
			}
		]
	},
	{
		path: '/answer/:id',
		name: 'answer',
		component: () => import( /* webpackChunkName: "about" */ '../views/answer.vue'),
		children: [{
			path: '/answer/submissions',
			name: 'submissions',
			component: () => import('../components/submissionRecord.vue')
		}]
	},
	{
		path: '/adminMain',
		name: 'adminMain',
		component: () => import('../views/adminMain.vue'),
		children: [{
			path: '/adminMain/adminUpload',
			name: 'adminUpload',
			component: () => import('../components/adminUpload.vue')
		},
			{
				path: '/adminMain/createContest',
				name: 'createContest',
				component: () => import('../components/createContest')
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
