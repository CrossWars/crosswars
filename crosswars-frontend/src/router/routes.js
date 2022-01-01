
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
    component: () => import('layouts/LoginLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/LoginPage.vue') }
    ]
  },
  {
    path: '/home',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/HomePage.vue') }
    ]
  },
  {
    path: '/create_group',
    name: 'create_group',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/CreateGroupPage.vue') }
    ]
  },
  {
    path: '/group/:groupID',
    name: 'group',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/GroupPage.vue') }
    ]
  },
  {
    path: '/user/:userID',
    name: 'user',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/UserPage.vue') }
    ]
  },
  {
    path: '/groupInvite/:groupID',
    name: 'groupInvite',
    component: () => import('layouts/MainLayout.vue'),
    children: [
      { path: '', component: () => import('src/pages/GroupInvitePage.vue') }
    ]
  },
  // Always leave this as last one,
  // but you can also remove it
  {
    path: '/:catchAll(.*)*',
    component: () => import('src/pages/Error404Page.vue')
  }
]

export default routes
