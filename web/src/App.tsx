import { createBrowserRouter, Navigate } from 'react-router-dom'
import { PrivateRoute } from '@/components/PrivateRoute'
import AppLayout from '@/components/AppLayout'

export const router = createBrowserRouter([
  {
    path: '/login',
    lazy: () => import('@/pages/LoginPage').then((m) => ({ Component: m.default })),
  },
  {
    path: '/register',
    lazy: () => import('@/pages/RegisterPage').then((m) => ({ Component: m.default })),
  },
  {
    path: '/verify-otp',
    lazy: () => import('@/pages/VerifyOtpPage').then((m) => ({ Component: m.default })),
  },
  {
    element: <PrivateRoute />,
    children: [
      {
        element: <AppLayout />,
        children: [
          {
            path: '/',
            lazy: () => import('@/pages/FeedPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/listings/new',
            lazy: () => import('@/pages/CreateListingPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/listings/:id',
            lazy: () => import('@/pages/ListingDetailPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/listings/:id/edit',
            lazy: () => import('@/pages/EditListingPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/users/:id',
            lazy: () => import('@/pages/UserProfilePage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/profile/me',
            lazy: () => import('@/pages/ProfilePage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/settings/profile',
            lazy: () => import('@/pages/EditProfilePage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/chat',
            lazy: () => import('@/pages/ChatListPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/chat/:threadId',
            lazy: () => import('@/pages/ChatPage').then((m) => ({ Component: m.default })),
          },
          {
            path: '/checkout/:listingId',
            lazy: () => import('@/pages/CheckoutPage').then((m) => ({ Component: m.default })),
          },
        ],
      },
    ],
  },
  { path: '*', element: <Navigate to="/" replace /> },
])
