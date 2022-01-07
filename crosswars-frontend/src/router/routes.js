
const routes = [
  {
    path: '/',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/IndexPage.vue') }
    ]
  },
  {
    path: '/login',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/LoginPage.vue') }
    ],
    meta: {
      guest: true,
    }
  },
  {
    path: '/home',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/HomePage.vue') }
    ],
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/create_group',
    name: 'create_group',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/CreateGroupPage.vue') }
    ],
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/group/:groupID',
    name: 'group',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/GroupPage.vue') }
    ],
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/user/:userID',
    name: 'user',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/UserPage.vue') }
    ],
    meta: {
      requiresAuth: true,
    }
  },
  {
    path: '/group_invite/:groupID',
    name: 'groupInvite',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/GroupInvitePage.vue') }
    ],
    meta: {
      requiresAuth: true,
    }
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('src/pages/Error404Page.vue')
  }
]

export default routes
